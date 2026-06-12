package com.safesolar.unit;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "consumer_units")
public class ConsumerUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 30)
    private String code;
    @Column(nullable = false, length = 120)
    private String name;
    @Column(nullable = false, length = 120)
    private String ownerName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private UnitType type;
    @Column(nullable = false, precision = 7, scale = 4)
    private BigDecimal fixedShare;
    @Column(nullable = false)
    private boolean active = true;
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected ConsumerUnit() {}

    public ConsumerUnit(String code, String name, String ownerName, UnitType type, BigDecimal fixedShare) {
        this.code = code;
        this.name = name;
        this.ownerName = ownerName;
        this.type = type;
        this.fixedShare = fixedShare;
    }

    @PrePersist
    void prePersist() { createdAt = Instant.now(); }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getOwnerName() { return ownerName; }
    public UnitType getType() { return type; }
    public BigDecimal getFixedShare() { return fixedShare; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
}
