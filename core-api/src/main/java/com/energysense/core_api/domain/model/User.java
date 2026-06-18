package com.energysense.core_api.domain.model;

import java.time.Instant;
import java.util.UUID;

/**
 * Modèle de domaine — représente un User uniquement en termes métier.
 * Aucune préoccupation de persistance ou de framework ici.
 */
public class User {

    private final UUID id;
    private final String email;
    private final String passwordHash;
    private final String role;
    private final Instant createdAt;

    public User(UUID id, String email, String passwordHash, String role, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = (role != null) ? role : "ROLE_USER";
        this.createdAt = (createdAt != null) ? createdAt : Instant.now();
    }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
    public Instant getCreatedAt() { return createdAt; }
}