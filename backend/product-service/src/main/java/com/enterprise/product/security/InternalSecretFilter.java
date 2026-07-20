package com.enterprise.product.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.Optional;

/**
 * Authenticates a request only if it carries the shared internal secret that
 * the gateway (and trusted peer services) inject. A direct call to this port
 * has no such header and therefore stays unauthenticated, so Spring Security
 * rejects it with 401.
 */
@Component
@Slf4j
public class InternalSecretFilter extends OncePerRequestFilter {

    public static final String SECRET_HEADER = "X-Internal-Secret";
    public static final String USER_HEADER = "X-Authenticated-User";

    @Value("${app.security.internal-secret}")
    private String internalSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String presented = request.getHeader(SECRET_HEADER);

        if (matches(presented)) {
            // The gateway has already vetted the caller; carry the identity it resolved.
            String user = Optional.ofNullable(request.getHeader(USER_HEADER))
                    .filter(value -> !value.isBlank())
                    .orElse("gateway");

            var authentication = new UsernamePasswordAuthenticationToken(
                    user, null, List.of(new SimpleGrantedAuthority("ROLE_INTERNAL")));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (presented != null) {
            log.warn("Rejected request to {} with an invalid internal secret", request.getRequestURI());
        }

        filterChain.doFilter(request, response);
    }

    private boolean matches(String presented) {
        if (presented == null || internalSecret == null) return false;
        // Constant-time comparison so response timing does not leak the secret.
        return MessageDigest.isEqual(
                presented.getBytes(StandardCharsets.UTF_8),
                internalSecret.getBytes(StandardCharsets.UTF_8));
    }
}
