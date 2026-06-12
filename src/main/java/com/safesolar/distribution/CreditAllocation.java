package com.safesolar.distribution;

import com.safesolar.unit.ConsumerUnit;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "credit_allocations")
public class CreditAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "distribution_id", nullable = false)
    private CreditDistribution distribution;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "consumer_unit_id", nullable = false)
    private ConsumerUnit unit;
    @Column(nullable = false, precision = 14, scale = 4)
    private BigDecimal creditKwh;
    @Column(nullable = false, precision = 7, scale = 4)
    private BigDecimal appliedShare;

    protected CreditAllocation() {}

    public CreditAllocation(ConsumerUnit unit, BigDecimal creditKwh, BigDecimal appliedShare) {
        this.unit = unit;
        this.creditKwh = creditKwh;
        this.appliedShare = appliedShare;
    }

    void attachTo(CreditDistribution distribution) { this.distribution = distribution; }
    public Long getId() { return id; }
    public ConsumerUnit getUnit() { return unit; }
    public BigDecimal getCreditKwh() { return creditKwh; }
    public BigDecimal getAppliedShare() { return appliedShare; }
}
