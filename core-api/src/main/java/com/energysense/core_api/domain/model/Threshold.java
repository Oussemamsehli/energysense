package com.energysense.core_api.domain.model;

import java.util.UUID;

public class Threshold {

    private final UUID id;
    private final UUID sensorId;
    private final double minValue;
    private final double maxValue;

    public Threshold(UUID id, UUID sensorId, double minValue, double maxValue) {
        this.id = id;
        this.sensorId = sensorId;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    /**
     * La règle métier vit ici, dans le domaine — pas éparpillée dans un service.
     * C'est ce qu'on appelle un "domaine riche" : l'objet sait répondre
     * à une question sur lui-même, plutôt que de juste stocker des données.
     */
    public boolean isBreachedBy(double value) {
        return value < minValue || value > maxValue;
    }

    public UUID getId() { return id; }
    public UUID getSensorId() { return sensorId; }
    public double getMinValue() { return minValue; }
    public double getMaxValue() { return maxValue; }
}