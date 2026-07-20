package com.enterprise.cart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient is the mandated HTTP client for inter-service calls; RestTemplate is
 * deliberately not used anywhere in this project.
 */
@Configuration
public class WebClientConfig {

    @Value("${product.service.base-url}")
    private String productServiceBaseUrl;

    @Value("${app.security.internal-secret}")
    private String internalSecret;

    @Bean
    public WebClient productServiceWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(productServiceBaseUrl)
                // product-service now trusts only callers carrying the shared
                // internal secret, so every cart -> product call presents it.
                .defaultHeader("X-Internal-Secret", internalSecret)
                .build();
    }
}
