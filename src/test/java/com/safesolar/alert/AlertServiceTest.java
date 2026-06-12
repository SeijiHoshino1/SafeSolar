package com.safesolar.alert;

import com.safesolar.unit.ConsumerUnit;
import com.safesolar.unit.ConsumerUnitService;
import com.safesolar.unit.UnitType;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AlertServiceTest {
    @Test
    void returnsOpenAlertsBySeverityThenCreationTime() {
        ConsumerUnit unit = new ConsumerUnit("UC-1", "Unidade", "Owner", UnitType.APARTMENT, BigDecimal.ONE);
        ReflectionTestUtils.setField(unit, "id", 1L);
        Alert low = alert(unit, AlertSeverity.LOW, "2026-06-10T09:00:00Z");
        Alert critical = alert(unit, AlertSeverity.CRITICAL, "2026-06-10T10:00:00Z");
        AlertRepository repository = mock(AlertRepository.class);
        when(repository.findByStatusIn(anyList())).thenReturn(List.of(low, critical));

        var result = new AlertService(repository, mock(ConsumerUnitService.class)).prioritizedOpenAlerts();
        assertThat(result).extracting(AlertDtos.Response::severity)
                .containsExactly(AlertSeverity.CRITICAL, AlertSeverity.LOW);
    }

    private Alert alert(ConsumerUnit unit, AlertSeverity severity, String createdAt) {
        Alert alert = new Alert(unit, severity, "Titulo", "Mensagem");
        ReflectionTestUtils.setField(alert, "createdAt", Instant.parse(createdAt));
        return alert;
    }
}
