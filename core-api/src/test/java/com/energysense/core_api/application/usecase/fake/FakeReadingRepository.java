package com.energysense.core_api.application.usecase.fake;

import com.energysense.core_api.application.port.out.ReadingRepositoryPort;
import com.energysense.core_api.domain.model.Reading;

import java.time.Instant;
import java.util.*;

public class FakeReadingRepository implements ReadingRepositoryPort {

    private final List<Reading> store = new ArrayList<>();

    public List<Reading> getAll() {
        return store;
    }
    @Override
    public Optional<Reading> findLatestBySensorId(UUID sensorId) {
        return store.stream()
                .filter(r -> r.getSensorId().equals(sensorId))
                .max(Comparator.comparing(Reading::getRecordedAt));
    }

    @Override
    public Reading save(Reading reading) {
        store.add(reading);
        return reading;
    }

    @Override
    public List<Reading> findBySensorIdAndRange(UUID sensorId, Instant from, Instant to) {
        return store.stream()
                .filter(r -> r.getSensorId().equals(sensorId))
                .filter(r -> !r.getRecordedAt().isBefore(from) && !r.getRecordedAt().isAfter(to))
                .toList();
    }
}