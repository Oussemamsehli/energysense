package com.energysense.core_api.infrastructure.web.sensor;

import com.energysense.core_api.domain.model.Sensor;
import com.energysense.core_api.domain.model.SensorType;

import java.util.UUID;

public class SensorResponse {

    private final UUID id;
    private final UUID siteId;
    private final SensorType type;
    private final String unit;
    private final String mqttTopic;

    public SensorResponse(UUID id, UUID siteId, SensorType type, String unit, String mqttTopic) {
        this.id = id;
        this.siteId = siteId;
        this.type = type;
        this.unit = unit;
        this.mqttTopic = mqttTopic;
    }

    public static SensorResponse from(Sensor sensor) {
        return new SensorResponse(
                sensor.getId(),
                sensor.getSiteId(),
                sensor.getType(),
                sensor.getUnit(),
                sensor.getMqttTopic()
        );
    }

    public UUID getId() { return id; }
    public UUID getSiteId() { return siteId; }
    public SensorType getType() { return type; }
    public String getUnit() { return unit; }
    public String getMqttTopic() { return mqttTopic; }
}