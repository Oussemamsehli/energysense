package com.energysense.core_api.domain.model;

import java.util.UUID;

public class Sensor {

    private final UUID id;
    private final UUID siteId;
    private final SensorType type;
    private final String unit;
    private final String mqttTopic;

    public Sensor(UUID id, UUID siteId, SensorType type, String unit, String mqttTopic) {
        this.id = id;
        this.siteId = siteId;
        this.type = type;
        this.unit = unit;
        this.mqttTopic = mqttTopic;
    }

    public UUID getId() { return id; }
    public UUID getSiteId() { return siteId; }
    public SensorType getType() { return type; }
    public String getUnit() { return unit; }
    public String getMqttTopic() { return mqttTopic; }
}