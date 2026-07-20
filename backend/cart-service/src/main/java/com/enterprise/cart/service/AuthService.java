package com.enterprise.cart.service;

import com.enterprise.cart.dto.LoginResponse;
import com.enterprise.cart.exception.InvalidCredentialsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Demo-grade authentication.
 *
 * IMPORTANT: this gates the UI only. The REST endpoints are NOT protected —
 * there is no filter or Spring Security on the request path, so any client can
 * still call the APIs directly. Making this real means adding Spring Security
 * to both services and validating the token on every request.
 *
 * Credentials come from configuration rather than source, and tokens are held
 * in memory, so a restart invalidates every session.
 */
@Service
@Slf4j
public class AuthService {

    @Value("${app.auth.username}")
    private String configuredUsername;

    @Value("${app.auth.password}")
    private String configuredPassword;

    /** How long a session stays valid. Configurable so it can be tuned per environment. */
    @Value("${app.auth.session-ttl-minutes:480}")
    private long sessionTtlMinutes;

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    private record Session(String username, Instant expiresAt) {
    }

    public LoginResponse login(String username, String password) {
        // Constant-time comparison so response timing does not leak the password.
        boolean userMatches = constantTimeEquals(configuredUsername, username);
        boolean passwordMatches = constantTimeEquals(configuredPassword, password);

        if (!userMatches || !passwordMatches) {
            log.warn("Failed login attempt for username='{}'", username);
            throw new InvalidCredentialsException();
        }

        purgeExpired();

        String token = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plus(Duration.ofMinutes(sessionTtlMinutes));
        sessions.put(token, new Session(username, expiresAt));

        log.info("Login succeeded for username='{}', session expires {}", username, expiresAt);
        return LoginResponse.builder()
                .username(username)
                .token(token)
                .expiresAt(expiresAt)
                .build();
    }

    /**
     * Resolves a token to a username, or empty if unknown/expired.
     */
    public Optional<String> resolveToken(String token) {
        if (token == null || token.isBlank()) return Optional.empty();

        Session session = sessions.get(token);
        if (session == null) return Optional.empty();

        if (session.expiresAt().isBefore(Instant.now())) {
            sessions.remove(token);
            return Optional.empty();
        }
        return Optional.of(session.username());
    }

    public void logout(String token) {
        if (token != null && sessions.remove(token) != null) {
            log.info("Session ended");
        }
    }

    private void purgeExpired() {
        Instant now = Instant.now();
        sessions.entrySet().removeIf(entry -> entry.getValue().expiresAt().isBefore(now));
    }

    private boolean constantTimeEquals(String expected, String actual) {
        if (expected == null || actual == null) return false;
        return MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.UTF_8),
                actual.getBytes(StandardCharsets.UTF_8));
    }
}
