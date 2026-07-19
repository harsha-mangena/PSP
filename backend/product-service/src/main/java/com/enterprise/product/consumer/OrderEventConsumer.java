package com.enterprise.product.consumer;

import com.enterprise.product.event.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Notification/logging system for placed orders.
 */
@Component
@Slf4j
public class OrderEventConsumer {

    @KafkaListener(
            topics = "${app.kafka.order-events-topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "orderEventListenerFactory")
    public void consume(ConsumerRecord<String, OrderEvent> record) {
        OrderEvent event = record.value();

        log.info("ORDER PLACED [topic={} partition={} offset={} key={}]: "
                        + "order={} user={} total={} lines={} at {}",
                record.topic(), record.partition(), record.offset(), record.key(),
                event.getOrderNumber(), event.getUserId(), event.getTotalAmount(),
                event.getItemCount(), event.getPlacedAt());
    }
}
