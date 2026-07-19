package com.enterprise.product.controller;

import com.enterprise.product.dto.PagedResponse;
import com.enterprise.product.dto.ProductRequest;
import com.enterprise.product.dto.ProductResponse;
import com.enterprise.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * REST entry point for products. Delegates everything to ProductService and
 * never touches the repository directly.
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        log.info("POST /api/products name={}", request.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(request));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        log.info("GET /api/products");
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Integer id) {
        log.info("GET /api/products/{}", id);
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Integer id,
                                                         @Valid @RequestBody ProductRequest request) {
        log.info("PUT /api/products/{}", id);
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        log.info("DELETE /api/products/{}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Paginated + sorted listing, e.g.
     * GET /api/products/paged?page=0&size=5&sortBy=price&direction=desc
     */
    @GetMapping("/paged")
    public ResponseEntity<PagedResponse<ProductResponse>> getProductsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("GET /api/products/paged page={} size={} sortBy={} direction={}",
                page, size, sortBy, direction);
        return ResponseEntity.ok(productService.getProductsPaged(page, size, sortBy, direction));
    }

    @GetMapping("/in-stock")
    public ResponseEntity<List<ProductResponse>> getInStockProducts() {
        log.info("GET /api/products/in-stock");
        return ResponseEntity.ok(productService.getInStockProducts());
    }

    @GetMapping("/inventory-value")
    public ResponseEntity<Map<String, BigDecimal>> getInventoryValue() {
        log.info("GET /api/products/inventory-value");
        return ResponseEntity.ok(productService.getInventoryValueByProduct());
    }

    /**
     * Native query endpoint, e.g. GET /api/products/above-price?minPrice=100
     */
    @GetMapping("/above-price")
    public ResponseEntity<List<ProductResponse>> getProductsAbovePrice(
            @RequestParam BigDecimal minPrice) {
        log.info("GET /api/products/above-price minPrice={}", minPrice);
        return ResponseEntity.ok(productService.getProductsAbovePrice(minPrice));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponse>> getLowStockProducts(
            @RequestParam(defaultValue = "10") Integer threshold,
            @RequestParam(defaultValue = "5") Integer limit) {
        log.info("GET /api/products/low-stock threshold={} limit={}", threshold, limit);
        return ResponseEntity.ok(productService.getLowStockProducts(threshold, limit));
    }

    /**
     * Called by cart-service during checkout to decrement stock.
     */
    @PostMapping("/{id}/reduce-stock")
    public ResponseEntity<ProductResponse> reduceStock(@PathVariable Integer id,
                                                       @RequestParam Integer quantity) {
        log.info("POST /api/products/{}/reduce-stock quantity={}", id, quantity);
        return ResponseEntity.ok(productService.reduceStock(id, quantity));
    }

    /**
     * Consumed by cart-service during add-to-cart validation.
     */
    @GetMapping("/{id}/stock-check")
    public ResponseEntity<Boolean> checkStock(@PathVariable Integer id,
                                              @RequestParam Integer quantity) {
        log.info("GET /api/products/{}/stock-check quantity={}", id, quantity);
        return ResponseEntity.ok(productService.hasSufficientStock(id, quantity));
    }
}
