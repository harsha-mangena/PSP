package com.enterprise.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Integer id;

    private String orderNumber;

    private String userId;

    private BigDecimal totalAmount;

    private String status;

    private Instant placedAt;

    private List<OrderItemResponse> items;
}
