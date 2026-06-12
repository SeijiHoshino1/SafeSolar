package com.safesolar.distribution;

import com.safesolar.unit.ConsumerUnit;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CreditDistributionStrategy {
    DistributionStrategyType type();
    List<AllocationDraft> calculate(BigDecimal totalCredits, List<ConsumerUnit> units,
                                    Map<Long, BigDecimal> consumptionByUnit);
}
