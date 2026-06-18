package com.energysense.core_api.application.port.in;

import java.time.Instant;

/**
 * Port entrant : le use case que n'importe quel adapter "déclencheur"
 * (MQTT aujourd'hui, peut-être un endpoint REST "lecture manuelle" demain)
 * peut appeler.
 *
 * Remarque : la signature prend (topic, value, timestamp) déjà propres —
 * parser le JSON brut est le travail de l'adapter, pas de ce use case.
 * Le use case ne doit jamais savoir que MQTT ou JSON existent.
 */
public interface IngestReadingUseCase {
    void ingest(String mqttTopic, double value, Instant recordedAt);
}