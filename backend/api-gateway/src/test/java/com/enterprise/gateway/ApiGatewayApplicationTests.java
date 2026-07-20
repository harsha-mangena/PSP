package com.enterprise.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Verifies the gateway context (routes, filters, WebClient) wires up without a
 * live Eureka registry.
 */
@SpringBootTest
@ActiveProfiles("test")
class ApiGatewayApplicationTests {

    @Test
    void contextLoads() {
    }
}
