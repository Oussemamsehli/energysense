package com.energysense.core_api.application.port.in;

import com.energysense.core_api.domain.model.Sensor;
import com.energysense.core_api.domain.model.SensorType;

import java.util.UUID;

public interface CreateSensorUseCase {
    Sensor createSensor(UUID siteId, SensorType type, String unit, String mqttTopic);
}