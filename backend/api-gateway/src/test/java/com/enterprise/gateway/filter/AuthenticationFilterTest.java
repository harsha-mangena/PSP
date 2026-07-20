package com.enterprise.gateway.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Exercises the branches that do not depend on the remote validation call:
 * public-path passthrough, CORS preflight, and the missing-token rejection.
 * The valid/invalid-token paths run through WebClient and are covered by the
 * end-to-end curl checks against the running gateway.
 */
class AuthenticationFilterTest {

    private AuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new AuthenticationFilter(WebClient.builder());
        ReflectionTestUtils.setField(filter, "validationUrl", "http://localhost:8082/api/auth/me");
        ReflectionTestUtils.setField(filter, "publicPaths",
                List.of("/api/auth/login", "/api/auth/logout", "/api/auth/me"));
    }

    private GatewayFilterChain chainRecording(AtomicBoolean forwarded) {
        return exchange -> {
            forwarded.set(true);
            return Mono.empty();
        };
    }

    @Test
    @DisplayName("runs before routing")
    void ordersBeforeTheRoutingFilter() {
        assertThat(filter.getOrder()).isNegative();
    }

    @Test
    @DisplayName("a public path is forwarded without a token")
    void forwardsPublicPaths() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/api/auth/login"));
        AtomicBoolean forwarded = new AtomicBoolean(false);

        filter.filter(exchange, chainRecording(forwarded)).block();

        assertThat(forwarded).isTrue();
        assertThat(exchange.getResponse().getStatusCode()).isNull();
    }

    @Test
    @DisplayName("a CORS preflight is forwarded without a token")
    void forwardsPreflight() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.options("/api/products"));
        AtomicBoolean forwarded = new AtomicBoolean(false);

        filter.filter(exchange, chainRecording(forwarded)).block();

        assertThat(forwarded).isTrue();
    }

    @Test
    @DisplayName("a protected path without a token is rejected with 401 and not forwarded")
    void rejectsMissingToken() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/products"));
        AtomicBoolean forwarded = new AtomicBoolean(false);

        filter.filter(exchange, chainRecording(forwarded)).block();

        assertThat(forwarded).isFalse();
        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("a blank Authorization header is treated as missing")
    void rejectsBlankAuthorizationHeader() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/cart/root")
                        .header(HttpHeaders.AUTHORIZATION, "   "));
        AtomicBoolean forwarded = new AtomicBoolean(false);

        filter.filter(exchange, chainRecording(forwarded)).block();

        assertThat(forwarded).isFalse();
        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
