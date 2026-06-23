package com.energysense.core_api.application.usecase;

import com.energysense.core_api.application.usecase.fake.*;
import com.energysense.core_api.domain.model.Sensor;
import com.energysense.core_api.domain.model.SensorType;
import com.energysense.core_api.domain.model.Threshold;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class IngestReadingServiceTest {

    private FakeSensorRepository sensorRepo;
    private FakeReadingRepository readingRepo;
    private FakeThresholdRepository thresholdRepo;
    private FakeAlertRepository alertRepo;
    private IngestReadingService service;

    private static final String TOPIC = "energysense/test/temp";
    private static final UUID SENSOR_ID = UUID.randomUUID();
    private static final UUID SITE_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        sensorRepo = new FakeSensorRepository();
        readingRepo = new FakeReadingRepository();
        thresholdRepo = new FakeThresholdRepository();
        alertRepo = new FakeAlertRepository();
        service = new IngestReadingService(sensorRepo, readingRepo, thresholdRepo, alertRepo);
    }

    @Test
    void should_ignore_reading_when_topic_is_unknown() {
        service.ingest("unknown/topic", 25.0, Instant.now());

        assertTrue(readingRepo.getAll().isEmpty());
        assertTrue(alertRepo.getAll().isEmpty());
    }

    @Test
    void should_save_reading_when_value_is_normal() {
        Sensor sensor = new Sensor(SENSOR_ID, SITE_ID, SensorType.TEMPERATURE, "°C", TOPIC);
        sensorRepo.add(sensor);
        thresholdRepo.add(new Threshold(UUID.randomUUID(), SENSOR_ID, 10.0, 30.0));

        service.ingest(TOPIC, 20.0, Instant.now());

        assertEquals(1, readingRepo.getAll().size());
        assertTrue(alertRepo.getAll().isEmpty());
    }

    @Test
    void should_create_alert_when_threshold_is_breached() {
        Sensor sensor = new Sensor(SENSOR_ID, SITE_ID, SensorType.TEMPERATURE, "°C", TOPIC);
        sensorRepo.add(sensor);
        thresholdRepo.add(new Threshold(UUID.randomUUID(), SENSOR_ID, 10.0, 30.0));

        service.ingest(TOPIC, 50.0, Instant.now());

        assertEquals(1, readingRepo.getAll().size());
        assertEquals(1, alertRepo.getAll().size());
        assertFalse(alertRepo.getAll().get(0).isResolved());
    }
}