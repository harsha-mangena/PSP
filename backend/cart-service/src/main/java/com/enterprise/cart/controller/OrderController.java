package com.enterprise.cart.controller;

import com.enterprise.cart.dto.CheckoutRequest;
import com.enterprise.cart.dto.OrderResponse;
import com.enterprise.cart.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    /**
     * Mock payment: no gateway is called, but stock, persistence and the Kafka
     * event are all real.
     */
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(@Valid @RequestBody CheckoutRequest request) {
        log.info("POST /api/orders/checkout user={}", request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.checkout(request.getUserId()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrders(@PathVariable String userId) {
        log.info("GET /api/orders/{}", userId);
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }
}
