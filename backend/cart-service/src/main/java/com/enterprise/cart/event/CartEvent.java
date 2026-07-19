package com.enterprise.cart.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

/**
 * Event published to Kafka whenever a product is added to a cart.
 * Consumed by product-service.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CartEvent {

    private Integer cartId;

    private Integer productId;

    private Integer quantity;

    private String userId;

    private Instant occurredAt;
}
