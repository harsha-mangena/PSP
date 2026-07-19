package com.enterprise.product.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Consumer-side view of the order event published by cart-service.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderEvent {

    private Integer orderId;

    private String orderNumber;

    private String userId;

    private BigDecimal totalAmount;

    private Integer itemCount;

    private Instant placedAt;
}
