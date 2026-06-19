package com.energysense.core_api.application.usecase;

import com.energysense.core_api.application.port.in.CreateSensorUseCase;
import com.energysense.core_api.application.port.in.GetSensorsUseCase;
import com.energysense.core_api.application.port.out.SensorRepositoryPort;
import com.energysense.core_api.domain.model.Sensor;
import com.energysense.core_api.domain.model.SensorType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SensorService implements CreateSensorUseCase, GetSensorsUseCase {

    private final SensorRepositoryPort sensorRepositoryPort;

    public SensorService(SensorRepositoryPort sensorRepositoryPort) {
        this.sensorRepositoryPort = sensorRepositoryPort;
    }

    @Override
    public Sensor createSensor(UUID siteId, SensorType type, String unit, String mqttTopic) {
        Sensor sensor = new Sensor(UUID.randomUUID(), siteId, type, unit, mqttTopic);
        return sensorRepositoryPort.save(sensor);
    }

    @Override
    public List<Sensor> getSensorsBySite(UUID siteId) {
        return sensorRepositoryPort.findBySiteId(siteId);
    }
}