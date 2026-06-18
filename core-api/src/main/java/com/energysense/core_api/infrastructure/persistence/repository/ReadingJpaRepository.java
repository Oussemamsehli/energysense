package com.energysense.core_api.infrastructure.persistence.repository;

import com.energysense.core_api.infrastructure.persistence.entity.ReadingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReadingJpaRepository extends JpaRepository<ReadingJpaEntity, UUID> {

    List<ReadingJpaEntity> findBySensorIdAndRecordedAtBetweenOrderByRecordedAtAsc(
            UUID sensorId, Instant from, Instant to);

    ReadingJpaEntity findFirstBySensorIdOrderByRecordedAtDesc(UUID sensorId);
}