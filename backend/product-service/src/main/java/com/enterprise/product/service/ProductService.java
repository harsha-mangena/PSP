package com.enterprise.product.service;

import com.enterprise.product.dto.ProductRequest;
import com.enterprise.product.dto.ProductResponse;
import com.enterprise.product.entity.Product;
import com.enterprise.product.exception.ProductNotFoundException;
import com.enterprise.product.mapper.ProductMapper;
import com.enterprise.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * All product business logic lives here. Controllers delegate to this class and
 * never touch the repository directly.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product saved = productRepository.save(ProductMapper.toEntity(request));
        log.info("Created product id={} name={}", saved.getId(), saved.getName());
        return ProductMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return ProductMapper.toResponse(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponse updateProduct(Integer id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        Product updated = productRepository.save(product);
        log.info("Updated product id={}", updated.getId());
        return ProductMapper.toResponse(updated);
    }

    @Transactional
    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
        log.info("Deleted product id={}", id);
    }

    /**
     * Used by the cart flow to confirm a product can satisfy a requested quantity.
     */
    @Transactional(readOnly = true)
    public boolean hasSufficientStock(Integer id, Integer quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        boolean sufficient = product.getStock() >= quantity;
        log.info("Stock check product id={} requested={} available={} sufficient={}",
                id, quantity, product.getStock(), sufficient);
        return sufficient;
    }
}
