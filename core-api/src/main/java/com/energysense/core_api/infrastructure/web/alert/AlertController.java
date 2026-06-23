package com.energysense.core_api.infrastructure.web.alert;

import com.energysense.core_api.application.port.in.GetAlertsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sensors/{sensorId}/alerts")
public class AlertController {

    private final GetAlertsUseCase getAlertsUseCase;

    public AlertController(GetAlertsUseCase getAlertsUseCase) {
        this.getAlertsUseCase = getAlertsUseCase;
    }

    @GetMapping
    public ResponseEntity<List<AlertResponse>> getAlerts(@PathVariable UUID sensorId) {
        List<AlertResponse> alerts = getAlertsUseCase.getUnresolvedAlerts(sensorId)
                .stream()
                .map(AlertResponse::from)
                .toList();
        return ResponseEntity.ok(alerts);
    }
}