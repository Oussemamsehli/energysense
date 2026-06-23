package com.energysense.core_api.application.usecase.fake;

import com.energysense.core_api.application.port.out.SensorRepositoryPort;
import com.energysense.core_api.domain.model.Sensor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FakeSensorRepository implements SensorRepositoryPort {

    private final Map<UUID, Sensor> store = new HashMap<>();

    public void add(Sensor sensor) {
        store.put(sensor.getId(), sensor);
    }

    @Override
    public Sensor save(Sensor sensor) {
        store.put(sensor.getId(), sensor);
        return sensor;
    }
    @Override
    public Optional<Sensor> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Sensor> findByMqttTopic(String mqttTopic) {
        return store.values().stream()
                .filter(s -> s.getMqttTopic().equals(mqttTopic))
                .findFirst();
    }

    @Override
    public List<Sensor> findBySiteId(UUID siteId) {
        return store.values().stream()
                .filter(s -> s.getSiteId().equals(siteId))
                .toList();
    }
}