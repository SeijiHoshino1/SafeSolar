package com.safesolar.distribution;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "credit_distributions")
public class CreditDistribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDate periodStart;
    @Column(nullable = false)
    private LocalDate periodEnd;
    @Column(nullable = false, precision = 14, scale = 4)
    private BigDecimal totalCreditsKwh;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private DistributionStrategyType strategy;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DistributionStatus status;
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    @OneToMany(mappedBy = "distribution", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreditAllocation> allocations = new ArrayList<>();

    protected CreditDistribution() {}

    public CreditDistribution(LocalDate periodStart, LocalDate periodEnd, BigDecimal totalCreditsKwh,
                              DistributionStrategyType strategy) {
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.totalCreditsKwh = totalCreditsKwh;
        this.strategy = strategy;
        this.status = DistributionStatus.CALCULATED;
    }

    @PrePersist
    void prePersist() { createdAt = Instant.now(); }

    public void addAllocation(CreditAllocation allocation) {
        allocations.add(allocation);
        allocation.attachTo(this);
    }

    public void confirm() { status = DistributionStatus.CONFIRMED; }
    public Long getId() { return id; }
    public LocalDate getPeriodStart() { return periodStart; }
    public LocalDate getPeriodEnd() { return periodEnd; }
    public BigDecimal getTotalCreditsKwh() { return totalCreditsKwh; }
    public DistributionStrategyType getStrategy() { return strategy; }
    public DistributionStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public List<CreditAllocation> getAllocations() { return List.copyOf(allocations); }
}
