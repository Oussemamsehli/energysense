package com.energysense.core_api.infrastructure.persistence.adapter;

import com.energysense.core_api.application.port.out.ReadingRepositoryPort;
import com.energysense.core_api.domain.model.Reading;
import com.energysense.core_api.infrastructure.persistence.entity.ReadingJpaEntity;
import com.energysense.core_api.infrastructure.persistence.repository.ReadingJpaRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ReadingRepositoryAdapter implements ReadingRepositoryPort {

    private final ReadingJpaRepository jpaRepository;

    public ReadingRepositoryAdapter(ReadingJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Reading save(Reading reading) {
        UUID id = (reading.getId() != null) ? reading.getId() : UUID.randomUUID();
        ReadingJpaEntity entity = new ReadingJpaEntity(id, reading.getSensorId(),
                reading.getValue(), reading.getRecordedAt());
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public List<Reading> findBySensorIdAndRange(UUID sensorId, Instant from, Instant to) {
        return jpaRepository.findBySensorIdAndRecordedAtBetweenOrderByRecordedAtAsc(sensorId, from, to)
                .stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Reading> findLatestBySensorId(UUID sensorId) {
        return Optional.ofNullable(jpaRepository.findFirstBySensorIdOrderByRecordedAtDesc(sensorId))
                .map(this::toDomain);
    }

    private Reading toDomain(ReadingJpaEntity entity) {
        return new Reading(entity.getId(), entity.getSensorId(), entity.getValue(), entity.getRecordedAt());
    }
}