package com.safesolar.distribution;

import com.safesolar.unit.ConsumerUnit;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class ProportionalConsumptionStrategy implements CreditDistributionStrategy {
    @Override
    public DistributionStrategyType type() { return DistributionStrategyType.PROPORTIONAL_CONSUMPTION; }

    @Override
    public List<AllocationDraft> calculate(BigDecimal totalCredits, List<ConsumerUnit> units,
                                           Map<Long, BigDecimal> consumptionByUnit) {
        BigDecimal totalConsumption = consumptionByUnit.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return AllocationMath.allocate(totalCredits, units, consumptionByUnit, totalConsumption);
    }
}
