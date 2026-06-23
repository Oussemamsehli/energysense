package com.energysense.core_api.application.port.in;

import com.energysense.core_api.domain.model.Site;

import java.util.UUID;

public interface CreateSiteUseCase {
    Site createSite(String name, String location, UUID ownerId);
}