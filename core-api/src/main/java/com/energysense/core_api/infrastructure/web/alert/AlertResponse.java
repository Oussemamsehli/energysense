package com.energysense.core_api.infrastructure.web.alert;

import com.energysense.core_api.domain.model.Alert;

import java.time.Instant;
import java.util.UUID;

public class AlertResponse {

    private final UUID id;
    private final UUID sensorId;
    private final String message;
    private final Instant triggeredAt;
    private final boolean resolved;

    public AlertResponse(UUID id, UUID sensorId, String message, Instant triggeredAt, boolean resolved) {
        this.id = id;
        this.sensorId = sensorId;
        this.message = message;
        this.triggeredAt = triggeredAt;
        this.resolved = resolved;
    }

    public static AlertResponse from(Alert alert) {
        return new AlertResponse(
                alert.getId(),
                alert.getSensorId(),
                alert.getMessage(),
                alert.getTriggeredAt(),
                alert.isResolved()
        );
    }

    public UUID getId() { return id; }
    public UUID getSensorId() { return sensorId; }
    public String getMessage() { return message; }
    public Instant getTriggeredAt() { return triggeredAt; }
    public boolean isResolved() { return resolved; }
}