package com.enterprise.cart.producer;

import com.enterprise.cart.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Publishes order events to Kafka, keyed by userId so a customer's orders stay
 * ordered within a partition.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.order-events-topic}")
    private String topic;

    public void publishOrderEvent(OrderEvent event) {
        kafkaTemplate.send(topic, event.getUserId(), event).whenComplete((result, ex) -> {
            if (ex != null) {
                // The order is already committed; a publish failure must not fail the request.
                log.error("Failed to publish order event {}: {}", event, ex.getMessage(), ex);
            } else {
                log.info("Published order event to topic={} partition={} offset={} payload={}",
                        topic,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset(),
                        event);
            }
        });
    }
}
