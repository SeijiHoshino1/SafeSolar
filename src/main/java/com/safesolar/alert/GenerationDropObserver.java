package com.safesolar.alert;

import com.safesolar.reading.ReadingCreatedEvent;
import com.safesolar.reading.ReadingType;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class GenerationDropObserver {
    private static final BigDecimal DROP_THRESHOLD = new BigDecimal("0.40");
    private final AlertRepository alerts;

    public GenerationDropObserver(AlertRepository alerts) { this.alerts = alerts; }

    @EventListener
    public void onReading(ReadingCreatedEvent event) {
        if (event.previous() == null || event.current().getType() != ReadingType.GENERATION
                || event.previous().getEnergyKwh().signum() == 0) return;

        BigDecimal drop = event.previous().getEnergyKwh().subtract(event.current().getEnergyKwh())
                .divide(event.previous().getEnergyKwh(), 4, RoundingMode.HALF_UP);
        if (drop.compareTo(DROP_THRESHOLD) >= 0) {
            String percent = drop.multiply(BigDecimal.valueOf(100)).setScale(1, RoundingMode.HALF_UP) + "%";
            alerts.save(new Alert(event.current().getUnit(), AlertSeverity.HIGH, "Queda brusca de geracao",
                    "A geracao caiu " + percent + " em relacao a leitura anterior."));
        }
    }
}
