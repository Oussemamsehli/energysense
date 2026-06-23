package com.energysense.core_api.application.port.in;

import com.energysense.core_api.domain.model.Site;

import java.util.List;
import java.util.UUID;

public interface GetSitesUseCase {
    List<Site> getSitesByOwner(UUID ownerId);
}