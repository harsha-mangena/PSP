package com.enterprise.product.service;

import com.enterprise.product.dto.PagedResponse;
import com.enterprise.product.dto.ProductRequest;
import com.enterprise.product.dto.ProductResponse;
import com.enterprise.product.entity.Product;
import com.enterprise.product.exception.InsufficientStockException;
import com.enterprise.product.exception.ProductNotFoundException;
import com.enterprise.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private static Product product(Integer id, String name, String price, int stock) {
        return Product.builder()
                .id(id)
                .name(name)
                .price(new BigDecimal(price))
                .stock(stock)
                .build();
    }

    @Nested
    @DisplayName("createProduct")
    class CreateProduct {

        @Test
        void persistsAndReturnsTheSavedProduct() {
            ProductRequest request = new ProductRequest("Keyboard", new BigDecimal("89.99"), 40);
            when(productRepository.save(any(Product.class)))
                    .thenReturn(product(1, "Keyboard", "89.99", 40));

            ProductResponse response = productService.createProduct(request);

            assertThat(response.getId()).isEqualTo(1);
            assertThat(response.getName()).isEqualTo("Keyboard");
            assertThat(response.getPrice()).isEqualByComparingTo("89.99");
            assertThat(response.getStock()).isEqualTo(40);
        }

        @Test
        void mapsEveryRequestFieldOntoTheEntity() {
            ProductRequest request = new ProductRequest("Dock", new BigDecimal("199.00"), 8);
            when(productRepository.save(any(Product.class)))
                    .thenReturn(product(7, "Dock", "199.00", 8));

            productService.createProduct(request);

            ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
            verify(productRepository).save(captor.capture());
            Product saved = captor.getValue();
            assertThat(saved.getName()).isEqualTo("Dock");
            assertThat(saved.getPrice()).isEqualByComparingTo("199.00");
            assertThat(saved.getStock()).isEqualTo(8);
        }
    }

    @Nested
    @DisplayName("getProductById")
    class GetProductById {

        @Test
        void returnsTheProductWhenPresent() {
            when(productRepository.findById(1)).thenReturn(Optional.of(product(1, "Laptop", "1599.00", 7)));

            assertThat(productService.getProductById(1).getName()).isEqualTo("Laptop");
        }

        @Test
        void throwsWhenMissing() {
            when(productRepository.findById(404)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.getProductById(404))
                    .isInstanceOf(ProductNotFoundException.class)
                    .hasMessageContaining("404");
        }
    }

    @Nested
    @DisplayName("updateProduct")
    class UpdateProduct {

        @Test
        void overwritesAllMutableFields() {
            Product existing = product(1, "Old", "10.00", 1);
            when(productRepository.findById(1)).thenReturn(Optional.of(existing));
            when(productRepository.save(any(Product.class))).thenAnswer(call -> call.getArgument(0));

            ProductResponse response = productService.updateProduct(
                    1, new ProductRequest("New", new BigDecimal("25.50"), 9));

            assertThat(response.getName()).isEqualTo("New");
            assertThat(response.getPrice()).isEqualByComparingTo("25.50");
            assertThat(response.getStock()).isEqualTo(9);
        }

        @Test
        void throwsWhenMissing() {
            when(productRepository.findById(404)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.updateProduct(
                    404, new ProductRequest("x", BigDecimal.ONE, 1)))
                    .isInstanceOf(ProductNotFoundException.class);
            verify(productRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("deleteProduct")
    class DeleteProduct {

        @Test
        void deletesWhenPresent() {
            when(productRepository.existsById(1)).thenReturn(true);

            productService.deleteProduct(1);

            verify(productRepository).deleteById(1);
        }

        @Test
        void throwsAndDeletesNothingWhenMissing() {
            when(productRepository.existsById(404)).thenReturn(false);

            assertThatThrownBy(() -> productService.deleteProduct(404))
                    .isInstanceOf(ProductNotFoundException.class);
            verify(productRepository, never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("reduceStock")
    class ReduceStock {

        @Test
        void decrementsByTheRequestedQuantity() {
            Product existing = product(1, "Keyboard", "89.99", 40);
            when(productRepository.findById(1)).thenReturn(Optional.of(existing));
            when(productRepository.save(any(Product.class))).thenAnswer(call -> call.getArgument(0));

            ProductResponse response = productService.reduceStock(1, 3);

            assertThat(response.getStock()).isEqualTo(37);
        }

        @Test
        void allowsReducingToExactlyZero() {
            Product existing = product(1, "Dock", "199.00", 5);
            when(productRepository.findById(1)).thenReturn(Optional.of(existing));
            when(productRepository.save(any(Product.class))).thenAnswer(call -> call.getArgument(0));

            assertThat(productService.reduceStock(1, 5).getStock()).isZero();
        }

        @Test
        void refusesToDriveStockNegativeAndSavesNothing() {
            when(productRepository.findById(1)).thenReturn(Optional.of(product(1, "Dock", "199.00", 5)));

            assertThatThrownBy(() -> productService.reduceStock(1, 6))
                    .isInstanceOf(InsufficientStockException.class)
                    .hasMessageContaining("requested 6")
                    .hasMessageContaining("available 5");
            verify(productRepository, never()).save(any());
        }

        @Test
        void throwsWhenProductMissing() {
            when(productRepository.findById(404)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.reduceStock(404, 1))
                    .isInstanceOf(ProductNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("hasSufficientStock")
    class HasSufficientStock {

        @Test
        void trueWhenStockExceedsRequest() {
            when(productRepository.findById(1)).thenReturn(Optional.of(product(1, "K", "1.00", 10)));

            assertThat(productService.hasSufficientStock(1, 4)).isTrue();
        }

        @Test
        void trueWhenStockExactlyMeetsRequest() {
            when(productRepository.findById(1)).thenReturn(Optional.of(product(1, "K", "1.00", 4)));

            assertThat(productService.hasSufficientStock(1, 4)).isTrue();
        }

        @Test
        void falseWhenStockIsShort() {
            when(productRepository.findById(1)).thenReturn(Optional.of(product(1, "K", "1.00", 3)));

            assertThat(productService.hasSufficientStock(1, 4)).isFalse();
        }
    }

    @Nested
    @DisplayName("stream-based queries")
    class StreamQueries {

        @Test
        void inStockExcludesZeroStockAndSortsByName() {
            when(productRepository.findAll()).thenReturn(List.of(
                    product(1, "Monitor", "349.00", 15),
                    product(2, "Webcam", "59.99", 0),
                    product(3, "Dock", "199.00", 8)));

            List<ProductResponse> result = productService.getInStockProducts();

            assertThat(result).extracting(ProductResponse::getName)
                    .containsExactly("Dock", "Monitor");
        }

        @Test
        void inStockTreatsNullStockAsUnavailable() {
            Product nullStock = product(1, "Broken", "10.00", 0);
            nullStock.setStock(null);
            when(productRepository.findAll()).thenReturn(List.of(nullStock));

            assertThat(productService.getInStockProducts()).isEmpty();
        }

        @Test
        void inventoryValueMultipliesPriceByStock() {
            when(productRepository.findAll()).thenReturn(List.of(
                    product(1, "Laptop", "1599.00", 7),
                    product(2, "Webcam", "59.99", 0)));

            Map<String, BigDecimal> value = productService.getInventoryValueByProduct();

            assertThat(value.get("Laptop")).isEqualByComparingTo("11193.00");
            assertThat(value.get("Webcam")).isEqualByComparingTo("0.00");
        }
    }

    @Nested
    @DisplayName("pagination")
    class Pagination {

        @Test
        void mapsThePageEnvelopeOntoPagedResponse() {
            Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "id"));
            when(productRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(
                    List.of(product(1, "A", "1.00", 1), product(2, "B", "2.00", 2)),
                    pageable,
                    5));

            PagedResponse<ProductResponse> response =
                    productService.getProductsPaged(0, 2, "id", "asc");

            assertThat(response.getContent()).hasSize(2);
            assertThat(response.getTotalElements()).isEqualTo(5);
            assertThat(response.getTotalPages()).isEqualTo(3);
            assertThat(response.isFirst()).isTrue();
            assertThat(response.isLast()).isFalse();
        }

        @Test
        void descendingDirectionIsPushedIntoTheQuery() {
            when(productRepository.findAll(any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of()));

            productService.getProductsPaged(1, 5, "price", "desc");

            ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
            verify(productRepository).findAll(captor.capture());
            Sort.Order order = captor.getValue().getSort().getOrderFor("price");
            assertThat(order).isNotNull();
            assertThat(order.getDirection()).isEqualTo(Sort.Direction.DESC);
            assertThat(captor.getValue().getPageNumber()).isEqualTo(1);
        }

        @Test
        void unknownDirectionFallsBackToAscending() {
            when(productRepository.findAll(any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of()));

            productService.getProductsPaged(0, 5, "name", "sideways");

            ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
            verify(productRepository).findAll(captor.capture());
            assertThat(captor.getValue().getSort().getOrderFor("name").getDirection())
                    .isEqualTo(Sort.Direction.ASC);
        }
    }

    @Nested
    @DisplayName("native queries")
    class NativeQueries {

        @Test
        void abovePriceDelegatesToTheNativeQuery() {
            when(productRepository.findProductsAbovePrice(new BigDecimal("100")))
                    .thenReturn(List.of(product(1, "Laptop", "1599.00", 7)));

            List<ProductResponse> result =
                    productService.getProductsAbovePrice(new BigDecimal("100"));

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("Laptop");
        }

        @Test
        void lowStockPassesThresholdAndLimitThrough() {
            when(productRepository.findLowStockProducts(10, 3))
                    .thenReturn(List.of(product(5, "Webcam", "59.99", 0)));

            assertThat(productService.getLowStockProducts(10, 3)).hasSize(1);
            verify(productRepository).findLowStockProducts(10, 3);
        }
    }
}
