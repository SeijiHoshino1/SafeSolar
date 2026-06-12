package com.safesolar.report;

import java.math.BigDecimal;

public record EfficiencyReport(Long unitId, String unitCode, BigDecimal generationKwh,
                               BigDecimal consumptionKwh, BigDecimal balanceKwh) {}
