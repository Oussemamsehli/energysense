package com.energysense.core_api.infrastructure.web.threshold;

import com.energysense.core_api.domain.model.Threshold;

import java.util.UUID;

public class ThresholdResponse {

    private final UUID id;
    private final UUID sensorId;
    private final double minValue;
    private final double maxValue;

    public ThresholdResponse(UUID id, UUID sensorId, double minValue, double maxValue) {
        this.id = id;
        this.sensorId = sensorId;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public static ThresholdResponse from(Threshold threshold) {
        return new ThresholdResponse(
                threshold.getId(),
                threshold.getSensorId(),
                threshold.getMinValue(),
                threshold.getMaxValue()
        );
    }

    public UUID getId() { return id; }
    public UUID getSensorId() { return sensorId; }
    public double getMinValue() { return minValue; }
    public double getMaxValue() { return maxValue; }
}