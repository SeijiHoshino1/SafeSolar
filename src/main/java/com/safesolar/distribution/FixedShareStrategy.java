package com.safesolar.distribution;

import com.safesolar.shared.BusinessRuleException;
import com.safesolar.unit.ConsumerUnit;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FixedShareStrategy implements CreditDistributionStrategy {
    @Override
    public DistributionStrategyType type() { return DistributionStrategyType.FIXED_SHARE; }

    @Override
    public List<AllocationDraft> calculate(BigDecimal totalCredits, List<ConsumerUnit> units,
                                           Map<Long, BigDecimal> consumptionByUnit) {
        BigDecimal totalShares = units.stream().map(ConsumerUnit::getFixedShare)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalShares.subtract(BigDecimal.ONE).abs().compareTo(new BigDecimal("0.0001")) > 0) {
            throw new BusinessRuleException("As cotas fixas das unidades ativas devem somar 100%.");
        }
        Map<Long, BigDecimal> weights = units.stream()
                .collect(Collectors.toMap(ConsumerUnit::getId, ConsumerUnit::getFixedShare));
        return AllocationMath.allocate(totalCredits, units, weights, totalShares);
    }
}
