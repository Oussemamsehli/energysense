package com.energysense.core_api.application.port.in;

import com.energysense.core_api.domain.model.Alert;

import java.util.List;
import java.util.UUID;

public interface GetAlertsUseCase {
    List<Alert> getUnresolvedAlerts(UUID sensorId);
}