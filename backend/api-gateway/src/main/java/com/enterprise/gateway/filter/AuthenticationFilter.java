package com.enterprise.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

/**
 * Rejects unauthenticated API calls before they reach a service.
 *
 * Tokens live in cart-service's memory, so it is the only thing that can say
 * whether one is valid — the gateway asks it on every request rather than
 * caching. That costs one local hop per call but keeps sign-out immediate; a
 * cache would leave revoked tokens working until it expired.
 */
@Component
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final WebClient webClient;

    @Value("${app.auth.validation-url}")
    private String validationUrl;

    @Value("${app.public-paths}")
    private List<String> publicPaths;

    @Value("${app.security.internal-secret}")
    private String internalSecret;

    private static final String SECRET_HEADER = "X-Internal-Secret";
    private static final String USER_HEADER = "X-Authenticated-User";

    public AuthenticationFilter(WebClient.Builder loadBalancedWebClientBuilder) {
        this.webClient = loadBalancedWebClientBuilder.build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        // CORS preflight carries no credentials by design.
        if (request.getMethod() != null && "OPTIONS".equalsIgnoreCase(request.getMethod().name())) {
            return chain.filter(exchange);
        }

        // Public and authenticated requests alike are forwarded with the internal
        // secret, since the services now trust only callers that present it.
        if (isPublic(path)) {
            return chain.filter(withInternalHeaders(exchange, null));
        }

        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authorization == null || authorization.isBlank()) {
            log.warn("401 {} {} - no Authorization header", request.getMethod(), path);
            return unauthorized(exchange, "Missing bearer token");
        }

        return webClient.get()
                .uri(validationUrl)
                // The validation call also targets a now-secured service.
                .header(SECRET_HEADER, internalSecret)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .retrieve()
                .bodyToMono(java.util.Map.class)
                .flatMap(body -> {
                    String username = String.valueOf(body.get("username"));
                    return chain.filter(withInternalHeaders(exchange, username));
                })
                .onErrorResume(error -> {
                    log.warn("401 {} {} - token rejected: {}",
                            request.getMethod(), path, error.getMessage());
                    return unauthorized(exchange, "Invalid or expired token");
                });
    }

    /**
     * Stamps the internal secret (and, when known, the resolved user) onto the
     * request. Any client-supplied copies of these headers are removed first, so
     * a caller cannot forge trust by sending them to the gateway.
     */
    private ServerWebExchange withInternalHeaders(ServerWebExchange exchange, String username) {
        return exchange.mutate()
                .request(builder -> builder.headers(headers -> {
                    headers.remove(SECRET_HEADER);
                    headers.remove(USER_HEADER);
                    headers.set(SECRET_HEADER, internalSecret);
                    if (username != null) {
                        headers.set(USER_HEADER, username);
                    }
                }))
                .build();
    }

    private boolean isPublic(String path) {
        return publicPaths.stream().anyMatch(path::startsWith);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // Same JSON shape the services use, so clients parse errors one way.
        String body = String.format(
                "{\"timestamp\":\"%s\",\"status\":401,\"error\":\"Unauthorized\","
                        + "\"message\":\"%s\",\"path\":\"%s\"}",
                Instant.now(), message, exchange.getRequest().getPath().value());

        return response.writeWith(Mono.just(
                response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8))));
    }

    @Override
    public int getOrder() {
        // Run before the routing filter so rejected calls never reach a service.
        return -1;
    }
}
