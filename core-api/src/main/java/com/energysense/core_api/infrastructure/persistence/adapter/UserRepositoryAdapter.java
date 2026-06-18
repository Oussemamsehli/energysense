package com.energysense.core_api.infrastructure.persistence.adapter;

import com.energysense.core_api.application.port.out.UserRepositoryPort;
import com.energysense.core_api.domain.model.User;
import com.energysense.core_api.infrastructure.persistence.entity.UserJpaEntity;
import com.energysense.core_api.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository jpaRepository;

    public UserRepositoryAdapter(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User save(User user) {
        UUID id = (user.getId() != null) ? user.getId() : UUID.randomUUID();
        UserJpaEntity entity = new UserJpaEntity(id, user.getEmail(), user.getPasswordHash(),
                user.getRole(), user.getCreatedAt());
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    private User toDomain(UserJpaEntity entity) {
        return new User(entity.getId(), entity.getEmail(), entity.getPasswordHash(),
                entity.getRole(), entity.getCreatedAt());
    }
}