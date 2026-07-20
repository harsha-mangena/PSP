package com.enterprise.cart.service;

import com.enterprise.cart.client.ProductClient;
import com.enterprise.cart.dto.AddToCartRequest;
import com.enterprise.cart.dto.CartResponse;
import com.enterprise.cart.dto.ProductDto;
import com.enterprise.cart.entity.Cart;
import com.enterprise.cart.entity.CartItem;
import com.enterprise.cart.event.CartEvent;
import com.enterprise.cart.exception.CartItemNotFoundException;
import com.enterprise.cart.exception.InsufficientStockException;
import com.enterprise.cart.exception.ProductUnavailableException;
import com.enterprise.cart.producer.CartEventProducer;
import com.enterprise.cart.repository.CartItemRepository;
import com.enterprise.cart.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ProductClient productClient;
    @Mock
    private CartEventProducer cartEventProducer;

    private CartService cartService;

    @BeforeEach
    void setUp() {
        // Run the "async" work on the calling thread so assertions stay deterministic.
        // The production executor is a real pool; the joining logic is identical.
        cartService = new CartService(cartRepository, cartItemRepository, productClient,
                cartEventProducer, Runnable::run);
    }

    private static ProductDto productDto(Integer id, int stock) {
        return ProductDto.builder()
                .id(id)
                .name("Keyboard")
                .price(new BigDecimal("89.99"))
                .stock(stock)
                .build();
    }

    private static AddToCartRequest request(String user, Integer productId, Integer qty) {
        return new AddToCartRequest(user, productId, qty);
    }

    @Nested
    @DisplayName("addToCart")
    class AddToCart {

        @Test
        void createsACartForAFirstTimeUser() {
            when(productClient.getProductById(3)).thenReturn(productDto(3, 40));
            when(productClient.hasSufficientStock(3, 2)).thenReturn(true);
            when(cartRepository.findByUserId("alice")).thenReturn(Optional.empty());
            when(cartRepository.save(any(Cart.class)))
                    .thenReturn(Cart.builder().id(1).userId("alice").build());
            when(cartItemRepository.findByCartIdAndProductId(1, 3)).thenReturn(Optional.empty());
            when(cartItemRepository.findByCartId(1)).thenReturn(List.of());

            CartResponse response = cartService.addToCart(request("alice", 3, 2));

            assertThat(response.getCartId()).isEqualTo(1);
            verify(cartRepository).save(any(Cart.class));
        }

        @Test
        void reusesAnExistingCart() {
            Cart existing = Cart.builder().id(9).userId("bob").build();
            when(productClient.getProductById(3)).thenReturn(productDto(3, 40));
            when(productClient.hasSufficientStock(3, 1)).thenReturn(true);
            when(cartRepository.findByUserId("bob")).thenReturn(Optional.of(existing));
            when(cartItemRepository.findByCartIdAndProductId(9, 3)).thenReturn(Optional.empty());
            when(cartItemRepository.findByCartId(9)).thenReturn(List.of());

            cartService.addToCart(request("bob", 3, 1));

            verify(cartRepository, never()).save(any(Cart.class));
        }

        @Test
        void accumulatesQuantityWhenTheProductIsAlreadyInTheCart() {
            Cart cart = Cart.builder().id(1).userId("alice").build();
            CartItem existing = CartItem.builder().id(5).cartId(1).productId(3).quantity(2).build();

            when(productClient.getProductById(3)).thenReturn(productDto(3, 40));
            when(productClient.hasSufficientStock(3, 3)).thenReturn(true);
            when(cartRepository.findByUserId("alice")).thenReturn(Optional.of(cart));
            when(cartItemRepository.findByCartIdAndProductId(1, 3)).thenReturn(Optional.of(existing));
            when(cartItemRepository.findByCartId(1)).thenReturn(List.of(existing));

            cartService.addToCart(request("alice", 3, 3));

            ArgumentCaptor<CartItem> captor = ArgumentCaptor.forClass(CartItem.class);
            verify(cartItemRepository).save(captor.capture());
            assertThat(captor.getValue().getQuantity()).isEqualTo(5);
        }

        @Test
        void publishesAnEventDescribingWhatWasAdded() {
            Cart cart = Cart.builder().id(1).userId("alice").build();
            when(productClient.getProductById(3)).thenReturn(productDto(3, 40));
            when(productClient.hasSufficientStock(3, 2)).thenReturn(true);
            when(cartRepository.findByUserId("alice")).thenReturn(Optional.of(cart));
            when(cartItemRepository.findByCartIdAndProductId(1, 3)).thenReturn(Optional.empty());
            when(cartItemRepository.findByCartId(1)).thenReturn(List.of());

            cartService.addToCart(request("alice", 3, 2));

            ArgumentCaptor<CartEvent> captor = ArgumentCaptor.forClass(CartEvent.class);
            verify(cartEventProducer).publishCartEvent(captor.capture());
            CartEvent event = captor.getValue();
            assertThat(event.getCartId()).isEqualTo(1);
            assertThat(event.getProductId()).isEqualTo(3);
            assertThat(event.getQuantity()).isEqualTo(2);
            assertThat(event.getUserId()).isEqualTo("alice");
            assertThat(event.getOccurredAt()).isNotNull();
        }

        @Test
        void rejectsWhenStockIsShortAndPersistsNothing() {
            when(productClient.getProductById(3)).thenReturn(productDto(3, 40));
            when(productClient.hasSufficientStock(3, 999)).thenReturn(false);

            assertThatThrownBy(() -> cartService.addToCart(request("alice", 3, 999)))
                    .isInstanceOf(InsufficientStockException.class);

            verify(cartItemRepository, never()).save(any());
            verify(cartEventProducer, never()).publishCartEvent(any());
        }

        /**
         * The parallel validation wraps failures in CompletionException; the
         * service must unwrap so the original exception reaches the handler.
         */
        @Test
        void propagatesTheOriginalExceptionFromTheParallelLookup() {
            when(productClient.getProductById(9999))
                    .thenThrow(new ProductUnavailableException("Product not found: 9999"));

            assertThatThrownBy(() -> cartService.addToCart(request("alice", 9999, 1)))
                    .isInstanceOf(ProductUnavailableException.class)
                    .hasMessageContaining("9999");

            verify(cartItemRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("getCartByUserId")
    class GetCart {

        @Test
        void returnsAnEmptyCartRatherThanThrowingForANewUser() {
            when(cartRepository.findByUserId("nobody")).thenReturn(Optional.empty());

            CartResponse response = cartService.getCartByUserId("nobody");

            assertThat(response.getCartId()).isNull();
            assertThat(response.getUserId()).isEqualTo("nobody");
            assertThat(response.getItems()).isEmpty();
        }

        @Test
        void returnsTheStoredLines() {
            Cart cart = Cart.builder().id(1).userId("alice").build();
            when(cartRepository.findByUserId("alice")).thenReturn(Optional.of(cart));
            when(cartItemRepository.findByCartId(1)).thenReturn(List.of(
                    CartItem.builder().id(5).cartId(1).productId(3).quantity(2).build()));

            CartResponse response = cartService.getCartByUserId("alice");

            assertThat(response.getItems()).hasSize(1);
            assertThat(response.getItems().get(0).getQuantity()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("removeItem")
    class RemoveItem {

        @Test
        void deletesTheLine() {
            Cart cart = Cart.builder().id(1).userId("alice").build();
            CartItem item = CartItem.builder().id(5).cartId(1).productId(3).quantity(2).build();
            when(cartRepository.findByUserId("alice")).thenReturn(Optional.of(cart));
            when(cartItemRepository.findById(5)).thenReturn(Optional.of(item));
            when(cartItemRepository.findByCartId(1)).thenReturn(List.of());

            cartService.removeItem("alice", 5);

            verify(cartItemRepository).delete(item);
        }

        @Test
        void throwsWhenTheItemDoesNotExist() {
            when(cartRepository.findByUserId("alice"))
                    .thenReturn(Optional.of(Cart.builder().id(1).userId("alice").build()));
            when(cartItemRepository.findById(404)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> cartService.removeItem("alice", 404))
                    .isInstanceOf(CartItemNotFoundException.class);
        }

        /**
         * Guards against deleting a line out of somebody else's cart by id.
         */
        @Test
        void refusesToDeleteAnItemBelongingToAnotherCart() {
            Cart mine = Cart.builder().id(1).userId("alice").build();
            CartItem theirs = CartItem.builder().id(5).cartId(2).productId(3).quantity(2).build();
            when(cartRepository.findByUserId("alice")).thenReturn(Optional.of(mine));
            when(cartItemRepository.findById(5)).thenReturn(Optional.of(theirs));

            assertThatThrownBy(() -> cartService.removeItem("alice", 5))
                    .isInstanceOf(CartItemNotFoundException.class);
            verify(cartItemRepository, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("updateItemQuantity")
    class UpdateQuantity {

        @Test
        void setsAnAbsoluteQuantity() {
            Cart cart = Cart.builder().id(1).userId("alice").build();
            CartItem item = CartItem.builder().id(5).cartId(1).productId(3).quantity(2).build();
            when(cartRepository.findByUserId("alice")).thenReturn(Optional.of(cart));
            when(cartItemRepository.findById(5)).thenReturn(Optional.of(item));
            when(productClient.getProductById(3)).thenReturn(productDto(3, 40));
            when(cartItemRepository.findByCartId(1)).thenReturn(List.of(item));

            cartService.updateItemQuantity("alice", 5, 7);

            assertThat(item.getQuantity()).isEqualTo(7);
            verify(cartItemRepository).save(item);
        }

        @Test
        void rejectsAQuantityBeyondLiveStock() {
            Cart cart = Cart.builder().id(1).userId("alice").build();
            CartItem item = CartItem.builder().id(5).cartId(1).productId(3).quantity(2).build();
            when(cartRepository.findByUserId("alice")).thenReturn(Optional.of(cart));
            when(cartItemRepository.findById(5)).thenReturn(Optional.of(item));
            when(productClient.getProductById(3)).thenReturn(productDto(3, 5));

            assertThatThrownBy(() -> cartService.updateItemQuantity("alice", 5, 6))
                    .isInstanceOf(InsufficientStockException.class);

            verify(cartItemRepository, never()).save(any());
            assertThat(item.getQuantity()).isEqualTo(2);
        }

        @Test
        void refusesToTouchAnItemFromAnotherCart() {
            Cart mine = Cart.builder().id(1).userId("alice").build();
            CartItem theirs = CartItem.builder().id(5).cartId(2).productId(3).quantity(2).build();
            when(cartRepository.findByUserId("alice")).thenReturn(Optional.of(mine));
            when(cartItemRepository.findById(5)).thenReturn(Optional.of(theirs));

            assertThatThrownBy(() -> cartService.updateItemQuantity("alice", 5, 3))
                    .isInstanceOf(CartItemNotFoundException.class);
            verify(productClient, never()).getProductById(anyInt());
        }
    }
}
