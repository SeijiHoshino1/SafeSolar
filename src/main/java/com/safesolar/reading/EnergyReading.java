package com.safesolar.reading;

import com.safesolar.unit.ConsumerUnit;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "energy_readings", indexes = {
        @Index(name = "idx_reading_unit_time", columnList = "consumer_unit_id,recorded_at")
})
public class EnergyReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "consumer_unit_id", nullable = false)
    private ConsumerUnit unit;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReadingType type;
    @Column(nullable = false, precision = 14, scale = 4)
    private BigDecimal energyKwh;
    @Column(nullable = false)
    private Instant recordedAt;
    @Column(nullable = false, length = 30)
    private String source;

    protected EnergyReading() {}

    public EnergyReading(ConsumerUnit unit, ReadingType type, BigDecimal energyKwh, Instant recordedAt, String source) {
        this.unit = unit;
        this.type = type;
        this.energyKwh = energyKwh;
        this.recordedAt = recordedAt;
        this.source = source;
    }

    public Long getId() { return id; }
    public ConsumerUnit getUnit() { return unit; }
    public ReadingType getType() { return type; }
    public BigDecimal getEnergyKwh() { return energyKwh; }
    public Instant getRecordedAt() { return recordedAt; }
    public String getSource() { return source; }
}
