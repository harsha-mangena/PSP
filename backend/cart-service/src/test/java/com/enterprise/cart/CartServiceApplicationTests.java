package com.enterprise.cart;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Verifies the whole bean graph wires up. Runs against the "test" profile so it
 * needs neither SQL Server nor Kafka.
 */
@SpringBootTest
@ActiveProfiles("test")
class CartServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}
