package com.energysense.core_api.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "reading",
        indexes = {
                @Index(name = "idx_reading_sensor_time", columnList = "sensor_id, recordedAt")
        }
)
public class ReadingJpaEntity {

    @Id
    private UUID id;

    @Column(name = "sensor_id", nullable = false)
    private UUID sensorId;

    @Column(nullable = false)
    private Double value;

    @Column(nullable = false)
    private Instant recordedAt;

    protected ReadingJpaEntity() {
    }

    public ReadingJpaEntity(UUID id, UUID sensorId, Double value, Instant recordedAt) {
        this.id = id;
        this.sensorId = sensorId;
        this.value = value;
        this.recordedAt = recordedAt;
    }

    public UUID getId() { return id; }
    public UUID getSensorId() { return sensorId; }
    public Double getValue() { return value; }
    public Instant getRecordedAt() { return recordedAt; }
}