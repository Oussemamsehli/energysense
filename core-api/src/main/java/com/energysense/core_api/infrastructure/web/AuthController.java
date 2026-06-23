package com.energysense.core_api.infrastructure.web;

import com.energysense.core_api.application.command.LoginCommand;
import com.energysense.core_api.application.command.RegisterCommand;
import com.energysense.core_api.application.port.in.LoginUseCase;
import com.energysense.core_api.application.port.in.RegisterUserUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Adapter web : expose les use cases d'authentification via HTTP.
 * Ne contient aucune logique métier — juste la traduction HTTP ↔ use case.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase,
                          LoginUseCase loginUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterCommand command) {
        String email = registerUserUseCase.register(command);
        return ResponseEntity.ok(Map.of("message", "User created", "email", email));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginCommand command) {
        String token = loginUseCase.login(command);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
