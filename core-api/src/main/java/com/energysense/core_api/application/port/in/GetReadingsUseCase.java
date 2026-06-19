package com.energysense.core_api.application.port.in;

import com.energysense.core_api.domain.model.Reading;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface GetReadingsUseCase {
    List<Reading> getReadings(UUID sensorId, Instant from, Instant to);
}