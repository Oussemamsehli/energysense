package com.energysense.core_api.application.port.in;

import com.energysense.core_api.domain.model.Sensor;

import java.util.List;
import java.util.UUID;

public interface GetSensorsUseCase {
    List<Sensor> getSensorsBySite(UUID siteId);
}