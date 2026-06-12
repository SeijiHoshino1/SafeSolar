package com.safesolar.distribution;

import com.safesolar.shared.BusinessRuleException;
import com.safesolar.unit.ConsumerUnit;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

final class AllocationMath {
    private static final int SCALE = 4;
    private AllocationMath() {}

    static List<AllocationDraft> allocate(BigDecimal total, List<ConsumerUnit> units,
                                          Map<Long, BigDecimal> weights, BigDecimal weightTotal) {
        if (units.isEmpty()) throw new BusinessRuleException("Nao existem unidades ativas para o rateio.");
        if (weightTotal.signum() <= 0) throw new BusinessRuleException("A soma dos pesos do rateio deve ser maior que zero.");

        List<AllocationDraft> result = new ArrayList<>();
        BigDecimal allocated = BigDecimal.ZERO;
        for (int i = 0; i < units.size(); i++) {
            ConsumerUnit unit = units.get(i);
            BigDecimal share = weights.getOrDefault(unit.getId(), BigDecimal.ZERO)
                    .divide(weightTotal, SCALE, RoundingMode.HALF_UP);
            BigDecimal credit = i == units.size() - 1
                    ? total.subtract(allocated).setScale(SCALE, RoundingMode.HALF_UP)
                    : total.multiply(share).setScale(SCALE, RoundingMode.HALF_UP);
            allocated = allocated.add(credit);
            result.add(new AllocationDraft(unit, credit, share));
        }
        return result;
    }
}
