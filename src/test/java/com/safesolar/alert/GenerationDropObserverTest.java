package com.safesolar.alert;

import com.safesolar.reading.*;
import com.safesolar.unit.ConsumerUnit;
import com.safesolar.unit.UnitType;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class GenerationDropObserverTest {
    @Test
    void createsHighAlertWhenGenerationDropsAtLeastFortyPercent() {
        AlertRepository repository = mock(AlertRepository.class);
        ConsumerUnit unit = new ConsumerUnit("UC-1", "Unidade", "Owner", UnitType.APARTMENT, BigDecimal.ONE);
        EnergyReading previous = new EnergyReading(unit, ReadingType.GENERATION, new BigDecimal("100"),
                Instant.parse("2026-06-10T10:00:00Z"), "IOT");
        EnergyReading current = new EnergyReading(unit, ReadingType.GENERATION, new BigDecimal("55"),
                Instant.parse("2026-06-10T11:00:00Z"), "IOT");

        new GenerationDropObserver(repository).onReading(new ReadingCreatedEvent(current, previous));

        verify(repository).save(argThat(alert -> alert.getSeverity() == AlertSeverity.HIGH
                && alert.getTitle().contains("Queda")));
    }
}
