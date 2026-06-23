package com.energysense.core_api.infrastructure.web.reading;

import com.energysense.core_api.domain.model.Reading;

import java.time.Instant;
import java.util.UUID;

public class ReadingResponse {

    private final UUID id;
    private final UUID sensorId;
    private final double value;
    private final Instant recordedAt;

    public ReadingResponse(UUID id, UUID sensorId, double value, Instant recordedAt) {
        this.id = id;
        this.sensorId = sensorId;
        this.value = value;
        this.recordedAt = recordedAt;
    }

    public static ReadingResponse from(Reading reading) {
        return new ReadingResponse(
                reading.getId(),
                reading.getSensorId(),
                reading.getValue(),
                reading.getRecordedAt()
        );
    }

    public UUID getId() { return id; }
    public UUID getSensorId() { return sensorId; }
    public double getValue() { return value; }
    public Instant getRecordedAt() { return recordedAt; }
}