package com.energysense.core_api.infrastructure.web.sensor;

import com.energysense.core_api.domain.model.SensorType;

public class SensorRequest {

    private SensorType type;
    private String unit;
    private String mqttTopic;

    public SensorRequest() {}

    public SensorType getType() { return type; }
    public String getUnit() { return unit; }
    public String getMqttTopic() { return mqttTopic; }
}