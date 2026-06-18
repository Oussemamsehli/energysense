package com.energysense.core_api.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Alert {

    private final UUID id;
    private final UUID sensorId;
    private final String message;
    private final Instant triggeredAt;
    private final boolean resolved;

    public Alert(UUID id, UUID sensorId, String message, Instant triggeredAt, boolean resolved) {
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