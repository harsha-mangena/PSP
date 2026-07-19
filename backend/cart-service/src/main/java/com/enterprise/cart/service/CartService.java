package com.enterprise.cart.service;

import com.enterprise.cart.client.ProductClient;
import com.enterprise.cart.dto.AddToCartRequest;
import com.enterprise.cart.dto.CartItemResponse;
import com.enterprise.cart.dto.CartResponse;
import com.enterprise.cart.dto.ProductDto;
import com.enterprise.cart.entity.Cart;
import com.enterprise.cart.entity.CartItem;
import com.enterprise.cart.exception.CartNotFoundException;
import com.enterprise.cart.exception.InsufficientStockException;
import com.enterprise.cart.repository.CartItemRepository;
import com.enterprise.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Cart business logic. Product lookup and stock validation are wired in at 1H
 * via WebClient; this step establishes the persistence-side behaviour.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductClient productClient;

    @Transactional
    public CartResponse addToCart(AddToCartRequest request) {
        // Call product-service over WebClient: the product must exist and have stock.
        ProductDto product = productClient.getProductById(request.getProductId());

        if (product.getStock() == null || product.getStock() < request.getQuantity()) {
            log.warn("Rejecting add-to-cart: product={} requested={} available={}",
                    request.getProductId(), request.getQuantity(), product.getStock());
            throw new InsufficientStockException(
                    request.getProductId(), request.getQuantity(), product.getStock());
        }

        log.info("Validated product={} name='{}' stock={} for requested qty={}",
                product.getId(), product.getName(), product.getStock(), request.getQuantity());

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

        return buildCartResponse(cart);
    }

    @Transactional(readOnly = true)
    public CartResponse getCartByUserId(String userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));
        return buildCartResponse(cart);
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
