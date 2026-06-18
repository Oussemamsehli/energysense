package com.energysense.core_api.application.port.out;

import com.energysense.core_api.domain.model.Reading;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadingRepositoryPort {
    Reading save(Reading reading);
    List<Reading> findBySensorIdAndRange(UUID sensorId, Instant from, Instant to);
    Optional<Reading> findLatestBySensorId(UUID sensorId);
}