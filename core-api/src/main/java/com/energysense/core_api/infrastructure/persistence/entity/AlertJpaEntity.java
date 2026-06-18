package com.energysense.core_api.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "alert")
public class AlertJpaEntity {

    @Id
    private UUID id;

    @Column(name = "sensor_id", nullable = false)
    private UUID sensorId;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Instant triggeredAt;

    @Column(nullable = false)
    private boolean resolved;

    protected AlertJpaEntity() {
    }

    public AlertJpaEntity(UUID id, UUID sensorId, String message, Instant triggeredAt, boolean resolved) {
        this.id = id;
        this.sensorId = sensorId;
        this.message = message;
        this.triggeredAt = triggeredAt;
        this.resolved = resolved;
    }

    public UUID getId() { return id; }
    public UUID getSensorId() { return sensorId; }
    public String getMessage() { return message; }
    public Instant getTriggeredAt() { return triggeredAt; }
    public boolean isResolved() { return resolved; }
}