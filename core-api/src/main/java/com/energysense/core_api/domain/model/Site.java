package com.energysense.core_api.domain.model;

import java.util.UUID;

public class Site {

    private final UUID id;
    private final String name;
    private final String location;
    private final UUID ownerId;

    public Site(UUID id, String name, String location, UUID ownerId) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.ownerId = ownerId;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public UUID getOwnerId() { return ownerId; }
}