package com.energysense.core_api.infrastructure.persistence.repository;

import com.energysense.core_api.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Pure plomberie Spring Data. Rien en dehors de infrastructure.persistence
 * ne doit jamais importer ceci directement — seul l'adapter s'en sert.
 */
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    Optional<UserJpaEntity> findByEmail(String email);
}