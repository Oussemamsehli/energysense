package com.energysense.core_api.application.port.out;

import com.energysense.core_api.domain.model.Site;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SiteRepositoryPort {
    Site save(Site site);
    Optional<Site> findById(UUID id);
    List<Site> findByOwnerId(UUID ownerId);
}