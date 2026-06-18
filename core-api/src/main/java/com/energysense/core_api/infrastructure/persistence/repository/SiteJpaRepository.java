package com.energysense.core_api.infrastructure.persistence.repository;

import com.energysense.core_api.infrastructure.persistence.entity.SiteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SiteJpaRepository extends JpaRepository<SiteJpaEntity, UUID> {
    List<SiteJpaEntity> findByOwnerId(UUID ownerId);
}