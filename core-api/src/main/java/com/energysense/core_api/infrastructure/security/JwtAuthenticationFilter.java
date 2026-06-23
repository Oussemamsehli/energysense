package com.energysense.core_api.infrastructure.security;
import com.energysense.core_api.application.port.out.UserRepositoryPort;
import com.energysense.core_api.domain.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = Logger.getLogger(JwtAuthenticationFilter.class.getName());
    private final JwtService jwtService;
    private final UserRepositoryPort userRepository;
    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    public JwtAuthenticationFilter(JwtService jwtService, UserRepositoryPort userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/auth/");
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
        log.warning("JWT token received, length: " + token.length());
        if (!jwtService.isTokenValid(token)) {
            log.warning("JWT token INVALID - rejected");
            filterChain.doFilter(request, response);
            return;
        }
        String email = jwtService.extractEmail(token);
        log.warning("JWT valid, email: " + email);
        User user = null;
        try {
            user = userRepository.findByEmail(email).orElse(null);
        } catch (Exception e) {
            System.err.println("JWT_DEBUG DB_ERROR " + e.getMessage());
        }
        if (user == null) {
            log.warning("User not found in DB: " + email);
            filterChain.doFilter(request, response);
            return;
        }
        log.warning("User found: " + email + " role: " + user.getRole());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(), null,
                        List.of(new SimpleGrantedAuthority(user.getRole()))
                );
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        filterChain.doFilter(request, response);
    }
}
