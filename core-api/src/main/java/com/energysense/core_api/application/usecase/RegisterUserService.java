package com.energysense.core_api.application.usecase;

import com.energysense.core_api.application.command.RegisterCommand;
import com.energysense.core_api.application.port.in.RegisterUserUseCase;
import com.energysense.core_api.application.port.out.UserRepositoryPort;
import com.energysense.core_api.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserService(UserRepositoryPort userRepository,
                                PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String register(RegisterCommand command) {
        if (userRepository.findByEmail(command.email()).isPresent()) {
            throw new IllegalArgumentException("Email already in use: " + command.email());
        }
        String hashedPassword = passwordEncoder.encode(command.password());
        User user = new User(UUID.randomUUID(), command.email(), hashedPassword, "ROLE_USER", Instant.now());
        userRepository.save(user);
        return user.getEmail();
    }
}
