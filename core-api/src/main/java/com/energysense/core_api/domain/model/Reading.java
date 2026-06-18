package com.energysense.core_api.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Reading {

    private final UUID id;
    private final UUID sensorId;
    private final double value;
    private final Instant recordedAt;

    public Reading(UUID id, UUID sensorId, double value, Instant recordedAt) {
        this.id = id;
        this.sensorId = sensorId;
        this.value = value;
        this.recordedAt = recordedAt;
    }

    public UUID getId() { return id; }
    public UUID getSensorId() { return sensorId; }
    public double getValue() { return value; }
    public Instant getRecordedAt() { return recordedAt; }
}