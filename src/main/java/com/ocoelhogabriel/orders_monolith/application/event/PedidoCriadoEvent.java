package com.ocoelhogabriel.orders_monolith.application.event;

import java.util.List;

public record PedidoCriadoEvent(
        String orderId,
        String customerEmail,
        long totalCents,
        List<Item> items
) {
    public record Item(String productSku, int quantity, long priceCents) {}
}