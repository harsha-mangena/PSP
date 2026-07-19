package com.enterprise.product.mapper;

import com.enterprise.product.dto.ProductRequest;
import com.enterprise.product.dto.ProductResponse;
import com.enterprise.product.entity.Product;

/**
 * Translates between the persistence model and the API model so that entities
 * never leak out of the service layer.
 */
public final class ProductMapper {

    private ProductMapper() {
    }

    public static Product toEntity(ProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();
    }

    public static ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }
}
