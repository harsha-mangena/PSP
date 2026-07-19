package com.enterprise.cart.controller;

import com.enterprise.cart.dto.AddToCartRequest;
import com.enterprise.cart.dto.CartResponse;
import com.enterprise.cart.dto.UpdateQuantityRequest;
import com.enterprise.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addToCart(@Valid @RequestBody AddToCartRequest request) {
        log.info("POST /api/cart/items user={} product={} qty={}",
                request.getUserId(), request.getProductId(), request.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addToCart(request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable String userId) {
        log.info("GET /api/cart/{}", userId);
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PutMapping("/{userId}/items/{itemId}")
    public ResponseEntity<CartResponse> updateQuantity(
            @PathVariable String userId,
            @PathVariable Integer itemId,
            @Valid @RequestBody UpdateQuantityRequest request) {
        log.info("PUT /api/cart/{}/items/{} quantity={}", userId, itemId, request.getQuantity());
        return ResponseEntity.ok(
                cartService.updateItemQuantity(userId, itemId, request.getQuantity()));
    }

    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable String userId,
                                                   @PathVariable Integer itemId) {
        log.info("DELETE /api/cart/{}/items/{}", userId, itemId);
        return ResponseEntity.ok(cartService.removeItem(userId, itemId));
    }
}
