package com.energysense.core_api.infrastructure.web.threshold;

import com.energysense.core_api.application.port.in.SetThresholdUseCase;
import com.energysense.core_api.domain.model.Threshold;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/sensors/{sensorId}/threshold")
public class ThresholdController {

    private final SetThresholdUseCase setThresholdUseCase;

    public ThresholdController(SetThresholdUseCase setThresholdUseCase) {
        this.setThresholdUseCase = setThresholdUseCase;
    }

    @PostMapping
    public ResponseEntity<ThresholdResponse> setThreshold(
            @PathVariable UUID sensorId,
            @RequestBody ThresholdRequest request) {
        Threshold threshold = setThresholdUseCase.setThreshold(
                sensorId,
                request.getMinValue(),
                request.getMaxValue()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ThresholdResponse.from(threshold));
    }
}