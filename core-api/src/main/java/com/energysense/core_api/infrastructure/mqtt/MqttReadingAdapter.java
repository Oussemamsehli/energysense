package com.energysense.core_api.infrastructure.mqtt;

import com.energysense.core_api.application.port.in.IngestReadingUseCase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Adapter déclencheur : le SEUL endroit de tout le projet qui sait que
 * les messages MQTT contiennent du JSON sous la forme
 * {"value": 21.5, "timestamp": "..."}. Il traduit ce format externe
 * en un appel propre (topic, value, timestamp) sur le use case.
 */
@Component
public class MqttReadingAdapter {

    private static final Logger log = Logger.getLogger(MqttReadingAdapter.class.getName());

    private final IngestReadingUseCase ingestReadingUseCase;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MqttReadingAdapter(IngestReadingUseCase ingestReadingUseCase) {
        this.ingestReadingUseCase = ingestReadingUseCase;
    }

    public void handle(String topic, String rawPayload) {
        try {
            JsonNode json = objectMapper.readTree(rawPayload);
            double value = json.get("value").asDouble();
            Instant recordedAt = json.has("timestamp")
                    ? Instant.parse(json.get("timestamp").asText())
                    : Instant.now();

            ingestReadingUseCase.ingest(topic, value, recordedAt);
        } catch (Exception e) {
            log.log(Level.WARNING, "Failed to parse MQTT payload on topic " + topic, e);
        }
    }
}