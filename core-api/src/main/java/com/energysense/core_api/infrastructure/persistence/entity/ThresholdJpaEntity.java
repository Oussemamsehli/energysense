package com.energysense.core_api.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "threshold")
public class ThresholdJpaEntity {

    @Id
    private UUID id;

    @Column(name = "sensor_id", nullable = false, unique = true)
    private UUID sensorId;

    @Column(nullable = false)
    private Double minValue;

    @Column(nullable = false)
    private Double maxValue;

    protected ThresholdJpaEntity() {
    }

    public ThresholdJpaEntity(UUID id, UUID sensorId, Double minValue, Double maxValue) {
        this.id = id;
        this.sensorId = sensorId;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public UUID getId() { return id; }
    public UUID getSensorId() { return sensorId; }
    public Double getMinValue() { return minValue; }
    public Double getMaxValue() { return maxValue; }
}