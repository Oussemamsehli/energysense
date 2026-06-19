package com.energysense.core_api.infrastructure.web.reading;

import com.energysense.core_api.application.port.in.GetReadingsUseCase;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sensors/{sensorId}/readings")
public class ReadingController {

    private final GetReadingsUseCase getReadingsUseCase;

    public ReadingController(GetReadingsUseCase getReadingsUseCase) {
        this.getReadingsUseCase = getReadingsUseCase;
    }

    @GetMapping
    public ResponseEntity<List<ReadingResponse>> getReadings(
            @PathVariable UUID sensorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        List<ReadingResponse> readings = getReadingsUseCase.getReadings(sensorId, from, to)
                .stream()
                .map(ReadingResponse::from)
                .toList();
        return ResponseEntity.ok(readings);
    }
}