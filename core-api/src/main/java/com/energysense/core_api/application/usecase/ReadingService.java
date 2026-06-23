package com.energysense.core_api.application.usecase;

import com.energysense.core_api.application.port.in.GetReadingsUseCase;
import com.energysense.core_api.application.port.out.ReadingRepositoryPort;
import com.energysense.core_api.domain.model.Reading;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ReadingService implements GetReadingsUseCase {

    private final ReadingRepositoryPort readingRepositoryPort;

    public ReadingService(ReadingRepositoryPort readingRepositoryPort) {
        this.readingRepositoryPort = readingRepositoryPort;
    }

    @Override
    public List<Reading> getReadings(UUID sensorId, Instant from, Instant to) {
        return readingRepositoryPort.findBySensorIdAndRange(sensorId, from, to);
    }
}