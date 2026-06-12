package com.safesolar.distribution;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public final class DistributionDtos {
    private DistributionDtos() {}

    public record CalculateRequest(@NotNull LocalDate periodStart, @NotNull LocalDate periodEnd,
                                   @NotNull @DecimalMin("0.0001") BigDecimal totalCreditsKwh,
                                   @NotNull DistributionStrategyType strategy) {}

    public record AllocationResponse(Long unitId, String unitCode, String unitName,
                                     BigDecimal creditKwh, BigDecimal appliedShare) {}

    public record Response(Long id, LocalDate periodStart, LocalDate periodEnd, BigDecimal totalCreditsKwh,
                           DistributionStrategyType strategy, DistributionStatus status, Instant createdAt,
                           List<AllocationResponse> allocations) {
        public static Response from(CreditDistribution distribution) {
            return new Response(distribution.getId(), distribution.getPeriodStart(), distribution.getPeriodEnd(),
                    distribution.getTotalCreditsKwh(), distribution.getStrategy(), distribution.getStatus(),
                    distribution.getCreatedAt(), distribution.getAllocations().stream()
                    .map(a -> new AllocationResponse(a.getUnit().getId(), a.getUnit().getCode(), a.getUnit().getName(),
                            a.getCreditKwh(), a.getAppliedShare())).toList());
        }
    }
}
