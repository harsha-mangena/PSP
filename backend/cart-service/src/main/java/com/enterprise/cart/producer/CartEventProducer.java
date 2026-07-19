package com.enterprise.cart.producer;

import com.enterprise.cart.event.CartEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Publishes cart events to Kafka. Keyed by cartId so all events for one cart
 * land on the same partition and stay ordered.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CartEventProducer {

    private final KafkaTemplate<String, CartEvent> kafkaTemplate;

    @Value("${app.kafka.cart-events-topic}")
    private String topic;

    public void publishCartEvent(CartEvent event) {
        String key = String.valueOf(event.getCartId());

        kafkaTemplate.send(topic, key, event).whenComplete((result, ex) -> {
            if (ex != null) {
                // The cart write already succeeded; a publish failure must not fail the request.
                log.error("Failed to publish cart event {}: {}", event, ex.getMessage(), ex);
            } else {
                log.info("Published cart event to topic={} partition={} offset={} payload={}",
                        topic,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset(),
                        event);
            }
        });
    }
}
