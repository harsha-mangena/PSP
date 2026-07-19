package com.enterprise.cart.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Published when an order is placed. Consumed by product-service.
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
