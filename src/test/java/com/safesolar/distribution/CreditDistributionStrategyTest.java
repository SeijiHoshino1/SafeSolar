package com.safesolar.distribution;

import com.safesolar.shared.BusinessRuleException;
import com.safesolar.unit.ConsumerUnit;
import com.safesolar.unit.UnitType;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;

class CreditDistributionStrategyTest {
    private final ConsumerUnit first = unit(1L, "AP-1", "0.4000");
    private final ConsumerUnit second = unit(2L, "AP-2", "0.6000");

    @Test
    void fixedShareMustDistributeTheExactTotal() {
        var result = new FixedShareStrategy().calculate(new BigDecimal("100.0000"), List.of(first, second), Map.of());
        assertThat(result).extracting(AllocationDraft::creditKwh)
                .containsExactly(new BigDecimal("40.0000"), new BigDecimal("60.0000"));
        assertThat(result.stream().map(AllocationDraft::creditKwh).reduce(BigDecimal.ZERO, BigDecimal::add))
                .isEqualByComparingTo("100.0000");
    }

    @Test
    void proportionalStrategyMustUseConsumptionAsWeightAndPreserveRemainder() {
        var consumption = Map.of(1L, new BigDecimal("1"), 2L, new BigDecimal("2"));
        var result = new ProportionalConsumptionStrategy().calculate(new BigDecimal("10.0000"),
                List.of(first, second), consumption);
        assertThat(result.get(0).creditKwh()).isEqualByComparingTo("3.3330");
        assertThat(result.get(1).creditKwh()).isEqualByComparingTo("6.6670");
        assertThat(result.stream().map(AllocationDraft::creditKwh).reduce(BigDecimal.ZERO, BigDecimal::add))
                .isEqualByComparingTo("10.0000");
    }

    @Test
    void proportionalStrategyMustRejectPeriodWithoutConsumption() {
        assertThatThrownBy(() -> new ProportionalConsumptionStrategy().calculate(BigDecimal.TEN,
                List.of(first, second), Map.of(1L, BigDecimal.ZERO, 2L, BigDecimal.ZERO)))
                .isInstanceOf(BusinessRuleException.class);
    }

    private ConsumerUnit unit(Long id, String code, String share) {
        ConsumerUnit unit = new ConsumerUnit(code, code, "Owner", UnitType.APARTMENT, new BigDecimal(share));
        ReflectionTestUtils.setField(unit, "id", id);
        return unit;
    }
}
