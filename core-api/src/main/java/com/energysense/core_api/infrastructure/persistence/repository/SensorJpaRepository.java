package com.energysense.core_api.infrastructure.persistence.repository;

import com.energysense.core_api.infrastructure.persistence.entity.SensorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SensorJpaRepository extends JpaRepository<SensorJpaEntity, UUID> {
    List<SensorJpaEntity> findBySiteId(UUID siteId);
    Optional<SensorJpaEntity> findByMqttTopic(String mqttTopic);
}