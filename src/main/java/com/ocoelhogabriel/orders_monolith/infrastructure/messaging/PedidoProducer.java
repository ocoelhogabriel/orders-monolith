package com.ocoelhogabriel.orders_monolith.infrastructure.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.ocoelhogabriel.orders_monolith.application.event.PedidoCriadoEvent;

@Component
public class PedidoProducer {

    private final KafkaTemplate<String, Object> kafka;

    public PedidoProducer(KafkaTemplate<String, Object> kafka) {
        this.kafka = kafka;
    }

    public void publish(PedidoCriadoEvent event, String topic) {
        kafka.send(topic, event.orderId(), event);
    }
}
