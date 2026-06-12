package com.safesolar.reading;

import com.safesolar.shared.BusinessRuleException;
import com.safesolar.unit.ConsumerUnit;
import com.safesolar.unit.ConsumerUnitService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;

@Service
public class EnergyReadingService {
    private final EnergyReadingRepository repository;
    private final ConsumerUnitService unitService;
    private final ApplicationEventPublisher events;

    public EnergyReadingService(EnergyReadingRepository repository, ConsumerUnitService unitService,
                                ApplicationEventPublisher events) {
        this.repository = repository;
        this.unitService = unitService;
        this.events = events;
    }

    @Transactional
    public ReadingDtos.Response create(ReadingDtos.CreateRequest request) {
        ConsumerUnit unit = unitService.require(request.unitId());
        EnergyReading previous = repository.findFirstByUnitIdAndTypeOrderByRecordedAtDesc(unit.getId(), request.type())
                .orElse(null);
        if (previous != null && request.recordedAt().isBefore(previous.getRecordedAt())) {
            throw new BusinessRuleException("A leitura nao pode ser anterior a ultima leitura do mesmo tipo.");
        }
        EnergyReading saved = repository.save(new EnergyReading(unit, request.type(), request.energyKwh(),
                request.recordedAt(), request.source().trim().toUpperCase()));
        events.publishEvent(new ReadingCreatedEvent(saved, previous));
        return ReadingDtos.Response.from(saved);
    }

    @Transactional(readOnly = true)
    public List<ReadingDtos.Response> history(Long unitId, Instant start, Instant end) {
        unitService.require(unitId);
        if (start.isAfter(end)) throw new BusinessRuleException("O inicio do periodo deve ser anterior ao fim.");
        return repository.findByUnitIdAndRecordedAtBetweenOrderByRecordedAtAsc(unitId, start, end).stream()
                .map(ReadingDtos.Response::from).toList();
    }
}
