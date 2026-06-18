package com.energysense.core_api.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "site")
public class SiteJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String location;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    protected SiteJpaEntity() {
    }

    public SiteJpaEntity(UUID id, String name, String location, UUID ownerId) {
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