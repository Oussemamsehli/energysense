package com.energysense.core_api.application.port.out;

import com.energysense.core_api.domain.model.Threshold;

import java.util.Optional;
import java.util.UUID;

public interface ThresholdRepositoryPort {
    Threshold save(Threshold threshold);
    Optional<Threshold> findBySensorId(UUID sensorId);
}