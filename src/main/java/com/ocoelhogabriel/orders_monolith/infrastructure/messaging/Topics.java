package com.ocoelhogabriel.orders_monolith.infrastructure.messaging;

public final class Topics {
    private Topics() {
    }

    public static final String PEDIDO_CRIADO = "${app.kafka.topic.pedidoCriado}";
}