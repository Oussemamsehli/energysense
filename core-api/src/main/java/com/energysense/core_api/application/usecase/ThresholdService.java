package com.energysense.core_api.application.usecase;

import com.energysense.core_api.application.port.in.SetThresholdUseCase;
import com.energysense.core_api.application.port.out.ThresholdRepositoryPort;
import com.energysense.core_api.domain.model.Threshold;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ThresholdService implements SetThresholdUseCase {

    private final ThresholdRepositoryPort thresholdRepositoryPort;

    public ThresholdService(ThresholdRepositoryPort thresholdRepositoryPort) {
        this.thresholdRepositoryPort = thresholdRepositoryPort;
    }

    @Override
    public Threshold setThreshold(UUID sensorId, double minValue, double maxValue) {
        Threshold threshold = new Threshold(UUID.randomUUID(), sensorId, minValue, maxValue);
        return thresholdRepositoryPort.save(threshold);
    }
}