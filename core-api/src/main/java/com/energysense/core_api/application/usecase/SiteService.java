package com.energysense.core_api.application.usecase;

import com.energysense.core_api.application.port.in.CreateSiteUseCase;
import com.energysense.core_api.application.port.in.GetSitesUseCase;
import com.energysense.core_api.application.port.out.SiteRepositoryPort;
import com.energysense.core_api.domain.model.Site;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SiteService implements CreateSiteUseCase, GetSitesUseCase {

    private final SiteRepositoryPort siteRepositoryPort;

    public SiteService(SiteRepositoryPort siteRepositoryPort) {
        this.siteRepositoryPort = siteRepositoryPort;
    }

    @Override
    public Site createSite(String name, String location, UUID ownerId) {
        Site site = new Site(UUID.randomUUID(), name, location, ownerId);
        return siteRepositoryPort.save(site);
    }

    @Override
    public List<Site> getSitesByOwner(UUID ownerId) {
        return siteRepositoryPort.findByOwnerId(ownerId);
    }
}