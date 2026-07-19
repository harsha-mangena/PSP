package com.enterprise.product.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

/**
 * Consumer-side view of the event published by cart-service. Kept as a separate
 * class so the two services stay independently deployable.
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
