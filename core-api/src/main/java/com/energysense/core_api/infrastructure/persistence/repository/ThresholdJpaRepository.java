package com.energysense.core_api.infrastructure.persistence.repository;

import com.energysense.core_api.infrastructure.persistence.entity.ThresholdJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ThresholdJpaRepository extends JpaRepository<ThresholdJpaEntity, UUID> {
    Optional<ThresholdJpaEntity> findBySensorId(UUID sensorId);
}