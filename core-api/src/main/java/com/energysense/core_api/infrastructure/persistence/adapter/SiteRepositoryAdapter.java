package com.energysense.core_api.infrastructure.persistence.adapter;

import com.energysense.core_api.application.port.out.SiteRepositoryPort;
import com.energysense.core_api.domain.model.Site;
import com.energysense.core_api.infrastructure.persistence.entity.SiteJpaEntity;
import com.energysense.core_api.infrastructure.persistence.repository.SiteJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class SiteRepositoryAdapter implements SiteRepositoryPort {

    private final SiteJpaRepository jpaRepository;

    public SiteRepositoryAdapter(SiteJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Site save(Site site) {
        UUID id = (site.getId() != null) ? site.getId() : UUID.randomUUID();
        SiteJpaEntity entity = new SiteJpaEntity(id, site.getName(), site.getLocation(), site.getOwnerId());
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Site> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Site> findByOwnerId(UUID ownerId) {
        return jpaRepository.findByOwnerId(ownerId).stream().map(this::toDomain).toList();
    }

    private Site toDomain(SiteJpaEntity entity) {
        return new Site(entity.getId(), entity.getName(), entity.getLocation(), entity.getOwnerId());
    }
}