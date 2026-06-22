package com.energysense.core_api.infrastructure.security;

import com.energysense.core_api.application.port.out.UserRepositoryPort;
import com.energysense.core_api.domain.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepositoryPort userRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepositoryPort userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        System.err.println("JWT_DEBUG token=" + token.substring(0,20));
        if (!jwtService.isTokenValid(token)) {
            System.err.println("JWT_DEBUG INVALID");
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtService.extractEmail(token);
        User user = userRepository.findByEmail(email).orElse(null);
        System.err.println("JWT_DEBUG email=" + email);
        if (user == null) {
            System.err.println("JWT_DEBUG USER_NULL");
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(), null,
                        List.of(new SimpleGrantedAuthority(user.getRole()))
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
