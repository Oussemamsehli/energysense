package com.energysense.core_api.infrastructure.persistence.adapter;

import com.energysense.core_api.application.port.out.ThresholdRepositoryPort;
import com.energysense.core_api.domain.model.Threshold;
import com.energysense.core_api.infrastructure.persistence.entity.ThresholdJpaEntity;
import com.energysense.core_api.infrastructure.persistence.repository.ThresholdJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ThresholdRepositoryAdapter implements ThresholdRepositoryPort {

    private final ThresholdJpaRepository jpaRepository;

    public ThresholdRepositoryAdapter(ThresholdJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Threshold save(Threshold threshold) {
        UUID id = (threshold.getId() != null) ? threshold.getId() : UUID.randomUUID();
        ThresholdJpaEntity entity = new ThresholdJpaEntity(id, threshold.getSensorId(),
                threshold.getMinValue(), threshold.getMaxValue());
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Threshold> findBySensorId(UUID sensorId) {
        return jpaRepository.findBySensorId(sensorId).map(this::toDomain);
    }

    private Threshold toDomain(ThresholdJpaEntity entity) {
        return new Threshold(entity.getId(), entity.getSensorId(), entity.getMinValue(), entity.getMaxValue());
    }
}