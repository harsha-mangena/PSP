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

    @Bean
    public WebClient productServiceWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(productServiceBaseUrl)
                .build();
    }
}
