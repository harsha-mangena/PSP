package com.enterprise.cart.client;

import com.enterprise.cart.dto.ProductDto;
import com.enterprise.cart.exception.ProductUnavailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Outbound calls to product-service. All HTTP goes through WebClient.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProductClient {

    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private final WebClient productServiceWebClient;

    /**
     * Fetches a product, translating a 404 into a domain exception so callers do
     * not have to reason about HTTP status codes.
     */
    public ProductDto getProductById(Integer productId) {
        log.info("WebClient GET /api/products/{}", productId);
        try {
            return productServiceWebClient.get()
                    .uri("/api/products/{id}", productId)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            response -> Mono.error(new ProductUnavailableException(
                                    "Product not found: " + productId)))
                    .onStatus(HttpStatusCode::is5xxServerError,
                            response -> Mono.error(new ProductUnavailableException(
                                    "product-service error while fetching product " + productId)))
                    .bodyToMono(ProductDto.class)
                    .block(TIMEOUT);
        } catch (WebClientRequestException e) {
            log.error("product-service unreachable for product {}: {}", productId, e.getMessage());
            throw new ProductUnavailableException(
                    "product-service is unreachable: " + e.getMessage());
        }
    }

    /**
     * Asks product-service to authoritatively confirm stock for a quantity.
     */
    public boolean hasSufficientStock(Integer productId, Integer quantity) {
        log.info("WebClient GET /api/products/{}/stock-check?quantity={}", productId, quantity);
        try {
            Boolean sufficient = productServiceWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/products/{id}/stock-check")
                            .queryParam("quantity", quantity)
                            .build(productId))
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            response -> Mono.error(new ProductUnavailableException(
                                    "Product not found: " + productId)))
                    .onStatus(HttpStatusCode::is5xxServerError,
                            response -> Mono.error(new ProductUnavailableException(
                                    "product-service error during stock check for " + productId)))
                    .bodyToMono(Boolean.class)
                    .block(TIMEOUT);
            return Boolean.TRUE.equals(sufficient);
        } catch (WebClientRequestException e) {
            log.error("product-service unreachable during stock check for {}: {}",
                    productId, e.getMessage());
            throw new ProductUnavailableException(
                    "product-service is unreachable: " + e.getMessage());
        }
    }
}
