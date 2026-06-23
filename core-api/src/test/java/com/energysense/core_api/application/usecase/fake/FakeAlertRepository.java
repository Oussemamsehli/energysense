package com.energysense.core_api.application.usecase.fake;

import com.energysense.core_api.application.port.out.AlertRepositoryPort;
import com.energysense.core_api.domain.model.Alert;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FakeAlertRepository implements AlertRepositoryPort {

    private final List<Alert> store = new ArrayList<>();

    public List<Alert> getAll() {
        return store;
    }

    @Override
    public Alert save(Alert alert) {
        store.add(alert);
        return alert;
    }

    @Override
    public List<Alert> findUnresolvedBySensorId(UUID sensorId) {
        return store.stream()
                .filter(a -> a.getSensorId().equals(sensorId))
                .filter(a -> !a.isResolved())
                .toList();
    }
}