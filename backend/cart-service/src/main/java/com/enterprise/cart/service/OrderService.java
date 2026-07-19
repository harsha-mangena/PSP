package com.enterprise.cart.service;

import com.enterprise.cart.client.ProductClient;
import com.enterprise.cart.dto.OrderItemResponse;
import com.enterprise.cart.dto.OrderResponse;
import com.enterprise.cart.dto.ProductDto;
import com.enterprise.cart.entity.Cart;
import com.enterprise.cart.entity.CartItem;
import com.enterprise.cart.entity.Order;
import com.enterprise.cart.entity.OrderItem;
import com.enterprise.cart.event.OrderEvent;
import com.enterprise.cart.exception.EmptyCartException;
import com.enterprise.cart.producer.OrderEventProducer;
import com.enterprise.cart.repository.CartItemRepository;
import com.enterprise.cart.repository.CartRepository;
import com.enterprise.cart.repository.OrderItemRepository;
import com.enterprise.cart.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Checkout and order history.
 *
 * Payment itself is mocked — there is no gateway call. Everything around it is
 * real: stock is decremented in product-service, the order is persisted with a
 * price snapshot, the cart is cleared, and an event is published to Kafka.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductClient productClient;
    private final OrderEventProducer orderEventProducer;

    @Transactional
    public OrderResponse checkout(String userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EmptyCartException(userId));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        if (cartItems.isEmpty()) {
            throw new EmptyCartException(userId);
        }

        // Reduce stock first. product-service validates each line, so if any
        // item is short the transaction rolls back before an order exists.
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            ProductDto product = productClient.reduceStock(
                    cartItem.getProductId(), cartItem.getQuantity());

            BigDecimal lineTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            total = total.add(lineTotal);

            orderItems.add(OrderItem.builder()
                    .productId(product.getId())
                    // Snapshot name and price so later catalogue edits do not
                    // rewrite what the customer actually paid.
                    .productName(product.getName())
                    .unitPrice(product.getPrice())
                    .quantity(cartItem.getQuantity())
                    .build());
        }

        Order order = orderRepository.save(Order.builder()
                .orderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .userId(userId)
                .totalAmount(total)
                .status("PAID")
                .placedAt(Instant.now())
                .build());

        orderItems.forEach(item -> item.setOrderId(order.getId()));
        orderItemRepository.saveAll(orderItems);

        // The cart is consumed by the order.
        cartItemRepository.deleteAll(cartItems);

        log.info("Placed order {} for user={} total={} lines={}",
                order.getOrderNumber(), userId, total, orderItems.size());

        orderEventProducer.publishOrderEvent(OrderEvent.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(userId)
                .totalAmount(total)
                .itemCount(orderItems.size())
                .placedAt(order.getPlacedAt())
                .build());

        return toResponse(order, orderItems);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUserId(String userId) {
        return orderRepository.findByUserIdOrderByPlacedAtDesc(userId).stream()
                .map(order -> toResponse(order, orderItemRepository.findByOrderId(order.getId())))
                .collect(Collectors.toList());
    }

    private OrderResponse toResponse(Order order, List<OrderItem> items) {
        List<OrderItemResponse> itemResponses = items.stream()
                .map(item -> OrderItemResponse.builder()
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .unitPrice(item.getUnitPrice())
                        .quantity(item.getQuantity())
                        .lineTotal(item.getUnitPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity())))
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUserId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .placedAt(order.getPlacedAt())
                .items(itemResponses)
                .build();
    }
}
