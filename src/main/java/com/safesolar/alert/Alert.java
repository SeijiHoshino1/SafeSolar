package com.safesolar.alert;

import com.safesolar.unit.ConsumerUnit;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "alerts", indexes = @Index(name = "idx_alert_status_severity", columnList = "status,severity"))
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "consumer_unit_id", nullable = false)
    private ConsumerUnit unit;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AlertSeverity severity;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AlertStatus status;
    @Column(nullable = false, length = 80)
    private String title;
    @Column(nullable = false, length = 500)
    private String message;
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    private Instant resolvedAt;

    protected Alert() {}

    public Alert(ConsumerUnit unit, AlertSeverity severity, String title, String message) {
        this.unit = unit;
        this.severity = severity;
        this.title = title;
        this.message = message;
        this.status = AlertStatus.OPEN;
    }

    @PrePersist
    void prePersist() { createdAt = Instant.now(); }
    public void acknowledge() { status = AlertStatus.ACKNOWLEDGED; }
    public void resolve() { status = AlertStatus.RESOLVED; resolvedAt = Instant.now(); }
    public Long getId() { return id; }
    public ConsumerUnit getUnit() { return unit; }
    public AlertSeverity getSeverity() { return severity; }
    public AlertStatus getStatus() { return status; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getResolvedAt() { return resolvedAt; }
}
