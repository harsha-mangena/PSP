package com.enterprise.product;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Verifies the whole bean graph wires up. Runs against the "test" profile so it
 * needs neither SQL Server nor Kafka.
 */
@SpringBootTest
@ActiveProfiles("test")
class ProductServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}
