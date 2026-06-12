package com.safesolar.distribution;

import com.safesolar.reading.EnergyReadingRepository;
import com.safesolar.reading.ReadingType;
import com.safesolar.shared.BusinessRuleException;
import com.safesolar.shared.NotFoundException;
import com.safesolar.unit.ConsumerUnit;
import com.safesolar.unit.ConsumerUnitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CreditDistributionService {
    private final CreditDistributionRepository repository;
    private final ConsumerUnitRepository units;
    private final EnergyReadingRepository readings;
    private final Map<DistributionStrategyType, CreditDistributionStrategy> strategies;

    public CreditDistributionService(CreditDistributionRepository repository, ConsumerUnitRepository units,
                                     EnergyReadingRepository readings, List<CreditDistributionStrategy> strategies) {
        this.repository = repository;
        this.units = units;
        this.readings = readings;
        this.strategies = strategies.stream().collect(Collectors.toUnmodifiableMap(CreditDistributionStrategy::type,
                Function.identity()));
    }

    @Transactional
    public DistributionDtos.Response calculate(DistributionDtos.CalculateRequest request) {
        if (request.periodStart().isAfter(request.periodEnd())) {
            throw new BusinessRuleException("O inicio do periodo deve ser anterior ao fim.");
        }
        List<ConsumerUnit> activeUnits = units.findByActiveTrueOrderByCodeAsc();
        var start = request.periodStart().atStartOfDay().toInstant(ZoneOffset.UTC);
        var end = request.periodEnd().plusDays(1).atStartOfDay().minusNanos(1).toInstant(ZoneOffset.UTC);
        Map<Long, BigDecimal> consumption = activeUnits.stream().collect(Collectors.toMap(ConsumerUnit::getId,
                unit -> readings.sumByUnitAndTypeAndPeriod(unit.getId(), ReadingType.CONSUMPTION, start, end)));
        CreditDistributionStrategy strategy = Optional.ofNullable(strategies.get(request.strategy()))
                .orElseThrow(() -> new BusinessRuleException("Estrategia de rateio nao suportada."));

        CreditDistribution distribution = new CreditDistribution(request.periodStart(), request.periodEnd(),
                request.totalCreditsKwh(), request.strategy());
        strategy.calculate(request.totalCreditsKwh(), activeUnits, consumption)
                .forEach(draft -> distribution.addAllocation(new CreditAllocation(draft.unit(), draft.creditKwh(), draft.share())));
        validateInvariant(distribution);
        return DistributionDtos.Response.from(repository.save(distribution));
    }

    @Transactional
    public DistributionDtos.Response confirm(Long id) {
        CreditDistribution distribution = require(id);
        if (distribution.getStatus() == DistributionStatus.CONFIRMED) {
            throw new BusinessRuleException("O rateio ja esta confirmado.");
        }
        validateInvariant(distribution);
        distribution.confirm();
        return DistributionDtos.Response.from(distribution);
    }

    @Transactional(readOnly = true)
    public List<DistributionDtos.Response> list() {
        return repository.findAllByOrderByCreatedAtDesc().stream().map(DistributionDtos.Response::from).toList();
    }

    private CreditDistribution require(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Rateio de creditos nao encontrado."));
    }

    private void validateInvariant(CreditDistribution distribution) {
        BigDecimal sum = distribution.getAllocations().stream().map(CreditAllocation::getCreditKwh)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (sum.compareTo(distribution.getTotalCreditsKwh()) != 0) {
            throw new BusinessRuleException("A soma dos creditos distribuidos difere do total disponivel.");
        }
    }
}
