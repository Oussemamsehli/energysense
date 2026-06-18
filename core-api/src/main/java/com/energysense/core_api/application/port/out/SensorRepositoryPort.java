package com.energysense.core_api.application.port.out;

import com.energysense.core_api.domain.model.Sensor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SensorRepositoryPort {
    Sensor save(Sensor sensor);
    Optional<Sensor> findById(UUID id);
    Optional<Sensor> findByMqttTopic(String mqttTopic);
    List<Sensor> findBySiteId(UUID siteId);
}