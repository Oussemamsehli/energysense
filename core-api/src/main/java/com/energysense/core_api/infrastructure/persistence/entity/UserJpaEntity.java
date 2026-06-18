package com.energysense.core_api.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Entité JPA — la représentation "base de données" d'un User.
 * Volontairement séparée de domain.model.User, qui n'a aucune
 * préoccupation de persistance.
 */
@Entity
@Table(name = "app_user")
public class UserJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected UserJpaEntity() {
        // requis par JPA
    }

    public UserJpaEntity(UUID id, String email, String passwordHash, String role, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
    public Instant getCreatedAt() { return createdAt; }
}