package com.energysense.core_api.application.usecase;

import com.energysense.core_api.application.port.in.GetAlertsUseCase;
import com.energysense.core_api.application.port.out.AlertRepositoryPort;
import com.energysense.core_api.domain.model.Alert;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AlertService implements GetAlertsUseCase {

    private final AlertRepositoryPort alertRepositoryPort;

    public AlertService(AlertRepositoryPort alertRepositoryPort) {
        this.alertRepositoryPort = alertRepositoryPort;
    }

    @Override
    public List<Alert> getUnresolvedAlerts(UUID sensorId) {
        return alertRepositoryPort.findUnresolvedBySensorId(sensorId);
    }
}