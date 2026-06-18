package com.energysense.core_api.application.usecase;

import com.energysense.core_api.application.port.in.IngestReadingUseCase;
import com.energysense.core_api.application.port.out.AlertRepositoryPort;
import com.energysense.core_api.application.port.out.ReadingRepositoryPort;
import com.energysense.core_api.application.port.out.SensorRepositoryPort;
import com.energysense.core_api.application.port.out.ThresholdRepositoryPort;
import com.energysense.core_api.domain.model.Alert;
import com.energysense.core_api.domain.model.Reading;
import com.energysense.core_api.domain.model.Sensor;
import com.energysense.core_api.domain.model.Threshold;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class IngestReadingService implements IngestReadingUseCase {

    private static final Logger log = Logger.getLogger(IngestReadingService.class.getName());

    private final SensorRepositoryPort sensorRepository;
    private final ReadingRepositoryPort readingRepository;
    private final ThresholdRepositoryPort thresholdRepository;
    private final AlertRepositoryPort alertRepository;

    public IngestReadingService(SensorRepositoryPort sensorRepository,
                                ReadingRepositoryPort readingRepository,
                                ThresholdRepositoryPort thresholdRepository,
                                AlertRepositoryPort alertRepository) {
        this.sensorRepository = sensorRepository;
        this.readingRepository = readingRepository;
        this.thresholdRepository = thresholdRepository;
        this.alertRepository = alertRepository;
    }

    @Override
    public void ingest(String mqttTopic, double value, Instant recordedAt) {
        Optional<Sensor> sensorOpt = sensorRepository.findByMqttTopic(mqttTopic);
        if (sensorOpt.isEmpty()) {
            log.warning("Reading received for unknown topic: " + mqttTopic);
            return;
        }
        Sensor sensor = sensorOpt.get();

        Reading reading = new Reading(UUID.randomUUID(), sensor.getId(), value, recordedAt);
        readingRepository.save(reading);

        thresholdRepository.findBySensorId(sensor.getId()).ifPresent(threshold ->
                checkThresholdAndRaiseAlertIfNeeded(sensor, threshold, value, recordedAt));
    }

    private void checkThresholdAndRaiseAlertIfNeeded(Sensor sensor, Threshold threshold,
                                                     double value, Instant recordedAt) {
        if (threshold.isBreachedBy(value)) {
            Alert alert = new Alert(
                    UUID.randomUUID(),
                    sensor.getId(),
                    "Value " + value + " " + sensor.getUnit() + " is outside range ["
                            + threshold.getMinValue() + ", " + threshold.getMaxValue() + "]",
                    recordedAt,
                    false
            );
            alertRepository.save(alert);
            log.warning("Threshold breached for sensor " + sensor.getId() + ": " + alert.getMessage());
        }
    }
}