package com.energysense.core_api.application.usecase.fake;

import com.energysense.core_api.application.port.out.ThresholdRepositoryPort;
import com.energysense.core_api.domain.model.Threshold;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FakeThresholdRepository implements ThresholdRepositoryPort {

    private final Map<UUID, Threshold> store = new HashMap<>();

    public void add(Threshold threshold) {
        store.put(threshold.getSensorId(), threshold);
    }

    @Override
    public Threshold save(Threshold threshold) {
        store.put(threshold.getSensorId(), threshold);
        return threshold;
    }

    @Override
    public Optional<Threshold> findBySensorId(UUID sensorId) {
        return Optional.ofNullable(store.get(sensorId));
    }
}