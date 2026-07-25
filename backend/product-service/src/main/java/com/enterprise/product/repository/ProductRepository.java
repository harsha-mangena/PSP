package com.enterprise.product.repository;

import com.enterprise.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    /**
     * Native SQL Server query: products priced above a threshold, most expensive first.
     */
    @Query(value = "SELECT * FROM products WHERE price > :minPrice ORDER BY price DESC",
            nativeQuery = true)
    List<Product> findProductsAbovePrice(@Param("minPrice") BigDecimal minPrice);

    /**
     * Native query using SQL Server specific syntax (TOP) for low-stock alerting.
     */
    @Query(value = "SELECT TOP (:limit) * FROM products WHERE stock < :threshold ORDER BY stock ASC",
            nativeQuery = true)
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold,
                                       @Param("limit") Integer limit);

    List<Product> findByCategoryOrderByName(String category);

    Page<Product> findByCategory(String category, Pageable pageable);

    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.category IS NOT NULL ORDER BY p.category")
    List<String> findDistinctCategories();
}
