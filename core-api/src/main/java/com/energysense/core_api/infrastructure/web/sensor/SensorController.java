package com.energysense.core_api.infrastructure.web.sensor;

import com.energysense.core_api.application.port.in.CreateSensorUseCase;
import com.energysense.core_api.application.port.in.GetSensorsUseCase;
import com.energysense.core_api.domain.model.Sensor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sites/{siteId}/sensors")
public class SensorController {

    private final CreateSensorUseCase createSensorUseCase;
    private final GetSensorsUseCase getSensorsUseCase;

    public SensorController(CreateSensorUseCase createSensorUseCase, GetSensorsUseCase getSensorsUseCase) {
        this.createSensorUseCase = createSensorUseCase;
        this.getSensorsUseCase = getSensorsUseCase;
    }

    @PostMapping
    public ResponseEntity<SensorResponse> createSensor(
            @PathVariable UUID siteId,
            @RequestBody SensorRequest request) {
        Sensor sensor = createSensorUseCase.createSensor(
                siteId,
                request.getType(),
                request.getUnit(),
                request.getMqttTopic()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(SensorResponse.from(sensor));
    }

    @GetMapping
    public ResponseEntity<List<SensorResponse>> getSensors(@PathVariable UUID siteId) {
        List<SensorResponse> sensors = getSensorsUseCase.getSensorsBySite(siteId)
                .stream()
                .map(SensorResponse::from)
                .toList();
        return ResponseEntity.ok(sensors);
    }
}