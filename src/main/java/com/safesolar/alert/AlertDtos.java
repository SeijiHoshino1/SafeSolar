package com.safesolar.alert;

import jakarta.validation.constraints.*;
import java.time.Instant;

public final class AlertDtos {
    private AlertDtos() {}

    public record CreateRequest(@NotNull Long unitId, @NotNull AlertSeverity severity,
                                @NotBlank @Size(max = 80) String title,
                                @NotBlank @Size(max = 500) String message) {}

    public record Response(Long id, Long unitId, String unitCode, AlertSeverity severity, AlertStatus status,
                           String title, String message, Instant createdAt, Instant resolvedAt) {
        public static Response from(Alert alert) {
            return new Response(alert.getId(), alert.getUnit().getId(), alert.getUnit().getCode(), alert.getSeverity(),
                    alert.getStatus(), alert.getTitle(), alert.getMessage(), alert.getCreatedAt(), alert.getResolvedAt());
        }
    }
}
