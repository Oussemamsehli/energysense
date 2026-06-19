package com.energysense.core_api.application.port.in;

import com.energysense.core_api.domain.model.Threshold;

import java.util.UUID;

public interface SetThresholdUseCase {
    Threshold setThreshold(UUID sensorId, double minValue, double maxValue);
}