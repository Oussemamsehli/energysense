package com.energysense.core_api.application.port.out;

import com.energysense.core_api.domain.model.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Port sortant : ce dont l'application a besoin pour persister des Users.
 * La couche infrastructure fournira l'implémentation concrète plus tard.
 */
public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
}