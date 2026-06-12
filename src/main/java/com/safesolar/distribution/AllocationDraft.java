package com.safesolar.distribution;

import com.safesolar.unit.ConsumerUnit;
import java.math.BigDecimal;

public record AllocationDraft(ConsumerUnit unit, BigDecimal creditKwh, BigDecimal share) {}
