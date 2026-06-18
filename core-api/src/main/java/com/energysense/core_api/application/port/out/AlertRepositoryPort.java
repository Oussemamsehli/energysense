package com.energysense.core_api.application.port.out;

import com.energysense.core_api.domain.model.Alert;

import java.util.List;
import java.util.UUID;

public interface AlertRepositoryPort {
    Alert save(Alert alert);
    List<Alert> findUnresolvedBySensorId(UUID sensorId);
}