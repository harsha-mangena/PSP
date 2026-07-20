package com.enterprise.cart.service;

import com.enterprise.cart.client.ProductClient;
import com.enterprise.cart.dto.OrderResponse;
import com.enterprise.cart.dto.ProductDto;
import com.enterprise.cart.entity.Cart;
import com.enterprise.cart.entity.CartItem;
import com.enterprise.cart.entity.Order;
import com.enterprise.cart.entity.OrderItem;
import com.enterprise.cart.event.OrderEvent;
import com.enterprise.cart.exception.EmptyCartException;
import com.enterprise.cart.exception.InsufficientStockException;
import com.enterprise.cart.producer.OrderEventProducer;
import com.enterprise.cart.repository.CartItemRepository;
import com.enterprise.cart.repository.CartRepository;
import com.enterprise.cart.repository.OrderItemRepository;
import com.enterprise.cart.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
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
class OrderServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private ProductClient productClient;
    @Mock
    private OrderEventProducer orderEventProducer;

    @InjectMocks
    private OrderService orderService;

    private static ProductDto product(Integer id, String name, String price, int stock) {
        return ProductDto.builder()
                .id(id).name(name).price(new BigDecimal(price)).stock(stock).build();
    }

    private void givenCartWith(List<CartItem> items) {
        Cart cart = Cart.builder().id(1).userId("alice").build();
        when(cartRepository.findByUserId("alice")).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(1)).thenReturn(items);
    }

    private void givenOrderIsSaved() {
        when(orderRepository.save(any(Order.class))).thenAnswer(call -> {
            Order order = call.getArgument(0);
            order.setId(100);
            return order;
        });
    }

    @Nested
    @DisplayName("checkout")
    class Checkout {

        @Test
        void totalsEveryLineAtTheLivePrice() {
            givenCartWith(List.of(
                    CartItem.builder().id(1).cartId(1).productId(3).quantity(2).build(),
                    CartItem.builder().id(2).cartId(1).productId(4).quantity(1).build()));
            when(productClient.reduceStock(3, 2)).thenReturn(product(3, "Keyboard", "89.99", 38));
            when(productClient.reduceStock(4, 1)).thenReturn(product(4, "Monitor", "349.00", 14));
            givenOrderIsSaved();

            OrderResponse response = orderService.checkout("alice");

            // 89.99 * 2 + 349.00 = 528.98
            assertThat(response.getTotalAmount()).isEqualByComparingTo("528.98");
            assertThat(response.getStatus()).isEqualTo("PAID");
            assertThat(response.getOrderNumber()).startsWith("ORD-");
            assertThat(response.getItems()).hasSize(2);
        }

        @Test
        void decrementsStockForEveryLine() {
            givenCartWith(List.of(
                    CartItem.builder().id(1).cartId(1).productId(3).quantity(2).build()));
            when(productClient.reduceStock(3, 2)).thenReturn(product(3, "Keyboard", "89.99", 38));
            givenOrderIsSaved();

            orderService.checkout("alice");

            verify(productClient).reduceStock(3, 2);
        }

        @Test
        void snapshotsNameAndPriceOntoTheOrderLine() {
            givenCartWith(List.of(
                    CartItem.builder().id(1).cartId(1).productId(3).quantity(2).build()));
            when(productClient.reduceStock(3, 2)).thenReturn(product(3, "Keyboard", "89.99", 38));
            givenOrderIsSaved();

            orderService.checkout("alice");

            ArgumentCaptor<List<OrderItem>> captor = ArgumentCaptor.forClass(List.class);
            verify(orderItemRepository).saveAll(captor.capture());
            OrderItem line = captor.getValue().get(0);
            assertThat(line.getProductName()).isEqualTo("Keyboard");
            assertThat(line.getUnitPrice()).isEqualByComparingTo("89.99");
            assertThat(line.getQuantity()).isEqualTo(2);
            assertThat(line.getOrderId()).isEqualTo(100);
        }

        @Test
        void clearsTheCartOnceOrdered() {
            List<CartItem> items = List.of(
                    CartItem.builder().id(1).cartId(1).productId(3).quantity(2).build());
            givenCartWith(items);
            when(productClient.reduceStock(3, 2)).thenReturn(product(3, "Keyboard", "89.99", 38));
            givenOrderIsSaved();

            orderService.checkout("alice");

            verify(cartItemRepository).deleteAll(items);
        }

        @Test
        void publishesAnOrderEvent() {
            givenCartWith(List.of(
                    CartItem.builder().id(1).cartId(1).productId(3).quantity(2).build()));
            when(productClient.reduceStock(3, 2)).thenReturn(product(3, "Keyboard", "89.99", 38));
            givenOrderIsSaved();

            orderService.checkout("alice");

            ArgumentCaptor<OrderEvent> captor = ArgumentCaptor.forClass(OrderEvent.class);
            verify(orderEventProducer).publishOrderEvent(captor.capture());
            OrderEvent event = captor.getValue();
            assertThat(event.getOrderId()).isEqualTo(100);
            assertThat(event.getUserId()).isEqualTo("alice");
            assertThat(event.getItemCount()).isEqualTo(1);
            assertThat(event.getTotalAmount()).isEqualByComparingTo("179.98");
        }

        @Test
        void rejectsAUserWithNoCart() {
            when(cartRepository.findByUserId("nobody")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.checkout("nobody"))
                    .isInstanceOf(EmptyCartException.class);
            verify(orderRepository, never()).save(any());
        }

        @Test
        void rejectsACartWithNoLines() {
            givenCartWith(List.of());

            assertThatThrownBy(() -> orderService.checkout("alice"))
                    .isInstanceOf(EmptyCartException.class);
            verify(productClient, never()).reduceStock(anyInt(), anyInt());
            verify(orderRepository, never()).save(any());
        }

        /**
         * Stock is reduced before the order row is written, so a shortfall must
         * leave no order behind  the transaction rolls back.
         */
        @Test
        void writesNoOrderWhenAnyLineIsShortOnStock() {
            givenCartWith(List.of(
                    CartItem.builder().id(1).cartId(1).productId(3).quantity(2).build(),
                    CartItem.builder().id(2).cartId(1).productId(4).quantity(99).build()));
            when(productClient.reduceStock(3, 2)).thenReturn(product(3, "Keyboard", "89.99", 38));
            when(productClient.reduceStock(4, 99))
                    .thenThrow(new InsufficientStockException(4, 99, 14));

            assertThatThrownBy(() -> orderService.checkout("alice"))
                    .isInstanceOf(InsufficientStockException.class);

            verify(orderRepository, never()).save(any());
            verify(orderItemRepository, never()).saveAll(any());
            verify(cartItemRepository, never()).deleteAll(any());
            verify(orderEventProducer, never()).publishOrderEvent(any());
        }
    }

    @Nested
    @DisplayName("getOrdersByUserId")
    class OrderHistory {

        @Test
        void returnsOrdersWithTheirLinesAndComputedLineTotals() {
            Order order = Order.builder()
                    .id(100).orderNumber("ORD-ABC").userId("alice")
                    .totalAmount(new BigDecimal("179.98")).status("PAID")
                    .placedAt(Instant.now()).build();
            when(orderRepository.findByUserIdOrderByPlacedAtDesc("alice")).thenReturn(List.of(order));
            when(orderItemRepository.findByOrderId(100)).thenReturn(List.of(
                    OrderItem.builder().id(1).orderId(100).productId(3)
                            .productName("Keyboard").unitPrice(new BigDecimal("89.99"))
                            .quantity(2).build()));

            List<OrderResponse> orders = orderService.getOrdersByUserId("alice");

            assertThat(orders).hasSize(1);
            assertThat(orders.get(0).getItems().get(0).getLineTotal())
                    .isEqualByComparingTo("179.98");
        }

        @Test
        void returnsEmptyWhenTheUserHasNeverOrdered() {
            when(orderRepository.findByUserIdOrderByPlacedAtDesc("nobody")).thenReturn(List.of());

            assertThat(orderService.getOrdersByUserId("nobody")).isEmpty();
        }
    }
}
