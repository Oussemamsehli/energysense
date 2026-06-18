package com.energysense.core_api.infrastructure.persistence.repository;

import com.energysense.core_api.infrastructure.persistence.entity.AlertJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AlertJpaRepository extends JpaRepository<AlertJpaEntity, UUID> {
    List<AlertJpaEntity> findBySensorIdAndResolvedFalse(UUID sensorId);
}