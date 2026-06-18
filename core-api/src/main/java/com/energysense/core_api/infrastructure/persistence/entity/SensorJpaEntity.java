package com.energysense.core_api.infrastructure.persistence.entity;

import com.energysense.core_api.domain.model.SensorType;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "sensor")
public class SensorJpaEntity {

    @Id
    private UUID id;

    @Column(name = "site_id", nullable = false)
    private UUID siteId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SensorType type;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false, unique = true)
    private String mqttTopic;

    protected SensorJpaEntity() {
    }

    public SensorJpaEntity(UUID id, UUID siteId, SensorType type, String unit, String mqttTopic) {
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