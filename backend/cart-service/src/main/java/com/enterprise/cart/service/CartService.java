package com.enterprise.cart.service;

import com.enterprise.cart.client.ProductClient;
import com.enterprise.cart.dto.AddToCartRequest;
import com.enterprise.cart.dto.CartItemResponse;
import com.enterprise.cart.dto.CartResponse;
import com.enterprise.cart.dto.ProductDto;
import com.enterprise.cart.entity.Cart;
import com.enterprise.cart.entity.CartItem;
import com.enterprise.cart.exception.CartNotFoundException;
import com.enterprise.cart.event.CartEvent;
import com.enterprise.cart.exception.InsufficientStockException;
import com.enterprise.cart.producer.CartEventProducer;
import com.enterprise.cart.repository.CartItemRepository;
import com.enterprise.cart.repository.CartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * Cart business logic: validates against product-service over WebClient,
 * persists the item, and publishes a Kafka event.
 */
@Service
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductClient productClient;
    private final CartEventProducer cartEventProducer;
    private final Executor cartTaskExecutor;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       ProductClient productClient,
                       CartEventProducer cartEventProducer,
                       @Qualifier("cartTaskExecutor") Executor cartTaskExecutor) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productClient = productClient;
        this.cartEventProducer = cartEventProducer;
        this.cartTaskExecutor = cartTaskExecutor;
    }

    @Transactional
    public CartResponse addToCart(AddToCartRequest request) {
        ProductDto product = fetchAndValidateInParallel(request);

        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseGet(() -> {
                    Cart created = cartRepository.save(
                            Cart.builder().userId(request.getUserId()).build());
                    log.info("Created cart id={} for user={}", created.getId(), created.getUserId());
                    return created;
                });

        // Adding the same product again accumulates quantity rather than duplicating the row.
        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), request.getProductId())
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + request.getQuantity());
                    return existing;
                })
                .orElseGet(() -> CartItem.builder()
                        .cartId(cart.getId())
                        .productId(request.getProductId())
                        .quantity(request.getQuantity())
                        .build());

        cartItemRepository.save(item);
        log.info("Added product={} qty={} to cart={}",
                request.getProductId(), request.getQuantity(), cart.getId());

        cartEventProducer.publishCartEvent(CartEvent.builder()
                .cartId(cart.getId())
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .userId(request.getUserId())
                .occurredAt(Instant.now())
                .build());

        return buildCartResponse(cart);
    }

    @Transactional(readOnly = true)
    public CartResponse getCartByUserId(String userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));
        return buildCartResponse(cart);
    }

    /**
     * Fetches the product and validates stock concurrently rather than in
     * sequence. Both are independent remote calls, so the wall-clock cost is the
     * slower of the two instead of their sum.
     */
    private ProductDto fetchAndValidateInParallel(AddToCartRequest request) {
        long startedAt = System.currentTimeMillis();

        CompletableFuture<ProductDto> productFuture = CompletableFuture.supplyAsync(
                () -> productClient.getProductById(request.getProductId()), cartTaskExecutor);

        CompletableFuture<Boolean> stockFuture = CompletableFuture.supplyAsync(
                () -> productClient.hasSufficientStock(
                        request.getProductId(), request.getQuantity()), cartTaskExecutor);

        try {
            // thenCombine joins both results once the slower of the two completes.
            ProductDto product = productFuture.thenCombine(stockFuture, (fetched, sufficient) -> {
                if (!Boolean.TRUE.equals(sufficient)) {
                    throw new InsufficientStockException(
                            request.getProductId(), request.getQuantity(), fetched.getStock());
                }
                return fetched;
            }).join();

            log.info("Parallel fetch+validate for product={} completed in {}ms",
                    request.getProductId(), System.currentTimeMillis() - startedAt);
            return product;

        } catch (CompletionException e) {
            // Unwrap so domain exceptions keep their intended HTTP status.
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException runtimeException) {
                log.warn("Parallel fetch+validate failed for product={}: {}",
                        request.getProductId(), cause.getMessage());
                throw runtimeException;
            }
            throw e;
        }
    }

    private CartResponse buildCartResponse(Cart cart) {
        List<CartItemResponse> items = cartItemRepository.findByCartId(cart.getId()).stream()
                .map(item -> CartItemResponse.builder()
                        .id(item.getId())
                        .cartId(item.getCartId())
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return CartResponse.builder()
                .cartId(cart.getId())
                .userId(cart.getUserId())
                .items(items)
                .build();
    }
}
