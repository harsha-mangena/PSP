package com.enterprise.product.consumer;

import com.enterprise.product.event.CartEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Notification/logging system for cart activity. Listens to the cart-events
 * topic and records what happened.
 */
@Component
@Slf4j
public class CartEventConsumer {

    @KafkaListener(
            topics = "${app.kafka.cart-events-topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "cartEventListenerFactory")
    public void consume(ConsumerRecord<String, CartEvent> record) {
        CartEvent event = record.value();

        log.info("Received cart event [topic={} partition={} offset={} key={}]: "
                        + "user={} added productId={} qty={} to cartId={} at {}",
                record.topic(), record.partition(), record.offset(), record.key(),
                event.getUserId(), event.getProductId(), event.getQuantity(),
                event.getCartId(), event.getOccurredAt());
    }
}
