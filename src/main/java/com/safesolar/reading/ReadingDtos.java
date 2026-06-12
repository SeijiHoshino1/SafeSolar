package com.safesolar.reading;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;

public final class ReadingDtos {
    private ReadingDtos() {}

    public record CreateRequest(@NotNull Long unitId, @NotNull ReadingType type,
                                @NotNull @DecimalMin("0.0000") BigDecimal energyKwh,
                                @NotNull @PastOrPresent Instant recordedAt,
                                @NotBlank @Size(max = 30) String source) {}

    public record Response(Long id, Long unitId, String unitCode, ReadingType type, BigDecimal energyKwh,
                           Instant recordedAt, String source) {
        public static Response from(EnergyReading reading) {
            return new Response(reading.getId(), reading.getUnit().getId(), reading.getUnit().getCode(),
                    reading.getType(), reading.getEnergyKwh(), reading.getRecordedAt(), reading.getSource());
        }
    }
}
