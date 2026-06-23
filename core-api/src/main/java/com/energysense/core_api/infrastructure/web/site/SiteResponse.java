package com.energysense.core_api.infrastructure.web.site;

import com.energysense.core_api.domain.model.Site;

import java.util.UUID;

public class SiteResponse {

    private final UUID id;
    private final String name;
    private final String location;

    public SiteResponse(UUID id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public static SiteResponse from(Site site) {
        return new SiteResponse(site.getId(), site.getName(), site.getLocation());
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
}