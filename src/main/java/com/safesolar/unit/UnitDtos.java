package com.safesolar.unit;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;

public final class UnitDtos {
    private UnitDtos() {}

    public record CreateRequest(
            @NotBlank @Size(max = 30) String code,
            @NotBlank @Size(max = 120) String name,
            @NotBlank @Size(max = 120) String ownerName,
            @NotNull UnitType type,
            @NotNull @DecimalMin("0.0001") @DecimalMax("1.0000") BigDecimal fixedShare) {}

    public record Response(Long id, String code, String name, String ownerName, UnitType type,
                           BigDecimal fixedShare, boolean active, Instant createdAt) {
        public static Response from(ConsumerUnit unit) {
            return new Response(unit.getId(), unit.getCode(), unit.getName(), unit.getOwnerName(), unit.getType(),
                    unit.getFixedShare(), unit.isActive(), unit.getCreatedAt());
        }
    }
}
