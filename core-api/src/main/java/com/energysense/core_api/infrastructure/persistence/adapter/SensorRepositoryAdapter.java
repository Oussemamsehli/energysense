package com.energysense.core_api.infrastructure.persistence.adapter;

import com.energysense.core_api.application.port.out.SensorRepositoryPort;
import com.energysense.core_api.domain.model.Sensor;
import com.energysense.core_api.infrastructure.persistence.entity.SensorJpaEntity;
import com.energysense.core_api.infrastructure.persistence.repository.SensorJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class SensorRepositoryAdapter implements SensorRepositoryPort {

    private final SensorJpaRepository jpaRepository;

    public SensorRepositoryAdapter(SensorJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Sensor save(Sensor sensor) {
        UUID id = (sensor.getId() != null) ? sensor.getId() : UUID.randomUUID();
        SensorJpaEntity entity = new SensorJpaEntity(id, sensor.getSiteId(), sensor.getType(),
                sensor.getUnit(), sensor.getMqttTopic());
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Sensor> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Sensor> findByMqttTopic(String mqttTopic) {
        return jpaRepository.findByMqttTopic(mqttTopic).map(this::toDomain);
    }

    @Override
    public List<Sensor> findBySiteId(UUID siteId) {
        return jpaRepository.findBySiteId(siteId).stream().map(this::toDomain).toList();
    }

    private Sensor toDomain(SensorJpaEntity entity) {
        return new Sensor(entity.getId(), entity.getSiteId(), entity.getType(),
                entity.getUnit(), entity.getMqttTopic());
    }
}