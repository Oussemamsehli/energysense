package com.energysense.core_api.infrastructure.persistence.adapter;

import com.energysense.core_api.application.port.out.AlertRepositoryPort;
import com.energysense.core_api.domain.model.Alert;
import com.energysense.core_api.infrastructure.persistence.entity.AlertJpaEntity;
import com.energysense.core_api.infrastructure.persistence.repository.AlertJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class AlertRepositoryAdapter implements AlertRepositoryPort {

    private final AlertJpaRepository jpaRepository;

    public AlertRepositoryAdapter(AlertJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Alert save(Alert alert) {
        UUID id = (alert.getId() != null) ? alert.getId() : UUID.randomUUID();
        AlertJpaEntity entity = new AlertJpaEntity(id, alert.getSensorId(), alert.getMessage(),
                alert.getTriggeredAt(), alert.isResolved());
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public List<Alert> findUnresolvedBySensorId(UUID sensorId) {
        return jpaRepository.findBySensorIdAndResolvedFalse(sensorId).stream().map(this::toDomain).toList();
    }

    private Alert toDomain(AlertJpaEntity entity) {
        return new Alert(entity.getId(), entity.getSensorId(), entity.getMessage(),
                entity.getTriggeredAt(), entity.isResolved());
    }
}
