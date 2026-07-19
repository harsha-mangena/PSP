package com.enterprise.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {

    private Integer productId;

    private String productName;

    private BigDecimal unitPrice;

    private Integer quantity;

    private BigDecimal lineTotal;
}
