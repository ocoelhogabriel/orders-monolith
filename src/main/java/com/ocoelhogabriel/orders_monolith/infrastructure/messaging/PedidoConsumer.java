package com.ocoelhogabriel.orders_monolith.infrastructure.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.ocoelhogabriel.orders_monolith.application.event.PedidoCriadoEvent;

@Component
public class PedidoConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PedidoConsumer.class);

    @KafkaListener(topics = "${app.kafka.topic.pedidoCriado}", groupId = "orders-group")
    public void onMessage(PedidoCriadoEvent event) {
        // logging/observabilidade; num sistema real, dispararia integração
        logger.info("[Kafka] PedidoCriado consumido: {}", event.orderId());
    }
}
