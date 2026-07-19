package com.enterprise.product.service;

import com.enterprise.product.dto.PagedResponse;
import com.enterprise.product.dto.ProductRequest;
import com.enterprise.product.dto.ProductResponse;
import com.enterprise.product.entity.Product;
import com.enterprise.product.exception.InsufficientStockException;
import com.enterprise.product.exception.ProductNotFoundException;
import com.enterprise.product.mapper.ProductMapper;
import com.enterprise.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
     * Database-level pagination and sorting. The sort is pushed into SQL Server
     * rather than applied in memory, so it stays correct across pages.
     */
    @Transactional(readOnly = true)
    public PagedResponse<ProductResponse> getProductsPaged(int page, int size,
                                                           String sortBy, String direction) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponse> content = productPage.getContent().stream()
                .map(ProductMapper::toResponse)
                .collect(Collectors.toList());

        log.info("Paged products page={} size={} sortBy={} direction={} totalElements={}",
                page, size, sortBy, direction, productPage.getTotalElements());

        return PagedResponse.<ProductResponse>builder()
                .content(content)
                .page(productPage.getNumber())
                .size(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .first(productPage.isFirst())
                .last(productPage.isLast())
                .build();
    }

    /**
     * Stream-based filtering: only products that are actually purchasable.
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getInStockProducts() {
        return productRepository.findAll().stream()
                .filter(product -> product.getStock() != null && product.getStock() > 0)
                .sorted(Comparator.comparing(Product::getName))
                .map(ProductMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Stream-based transformation: product name -> total value of stock on hand.
     */
    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getInventoryValueByProduct() {
        return productRepository.findAll().stream()
                .collect(Collectors.toMap(
                        Product::getName,
                        product -> product.getPrice()
                                .multiply(BigDecimal.valueOf(product.getStock())),
                        BigDecimal::add));
    }

    /**
     * Backed by a native SQL Server query (see ProductRepository).
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsAbovePrice(BigDecimal minPrice) {
        List<ProductResponse> results = productRepository.findProductsAbovePrice(minPrice).stream()
                .map(ProductMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Native query products above price={} returned {} rows", minPrice, results.size());
        return results;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getLowStockProducts(Integer threshold, Integer limit) {
        List<ProductResponse> results = productRepository.findLowStockProducts(threshold, limit).stream()
                .map(ProductMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Native query low stock threshold={} limit={} returned {} rows",
                threshold, limit, results.size());
        return results;
    }

    /**
     * Decrements stock at checkout. Guarded so concurrent orders cannot drive
     * stock negative; the check and the write share one transaction.
     */
    @Transactional
    public ProductResponse reduceStock(Integer id, Integer quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (product.getStock() < quantity) {
            throw new InsufficientStockException(id, quantity, product.getStock());
        }

        product.setStock(product.getStock() - quantity);
        Product saved = productRepository.save(product);
        log.info("Reduced stock for product id={} by {} -> {} remaining",
                id, quantity, saved.getStock());
        return ProductMapper.toResponse(saved);
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
