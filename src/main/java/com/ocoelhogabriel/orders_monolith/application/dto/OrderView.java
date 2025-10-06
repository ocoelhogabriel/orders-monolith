package com.ocoelhogabriel.orders_monolith.application.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "OrderView", description = "Visão de um pedido")
public record OrderView(
        @Schema(description = "Identificador do pedido", example = "a1b2c3d4") String id,
        @Schema(description = "Status do pedido", example = "PENDING") String status,
        @Schema(description = "Valor total em centavos", example = "1999") long totalCents,
        @Schema(description = "Email do cliente", example = "joao@example.com", format = "email") String customerEmail,
        @Schema(description = "Itens do pedido") List<Item> items) {

    @Schema(name = "OrderItemView", description = "Item dentro da visão de pedido")
    public record Item(@Schema(description = "SKU do produto", example = "SKU-12345") String productSku,
            @Schema(description = "Quantidade", example = "2") int quantity,
            @Schema(description = "Preço unitário em centavos", example = "999") long priceCents) {
    }

    // Builder estático para OrderView
    public static OrderViewBuilder builder() {
        return new OrderViewBuilder();
    }

    public static class OrderViewBuilder {
        private String id;
        private String status;
        private long totalCents;
        private String customerEmail;
        private final java.util.List<Item> items = new java.util.ArrayList<>();

        public OrderViewBuilder id(String id) {
            this.id = id;
            return this;
        }

        public OrderViewBuilder status(String status) {
            this.status = status;
            return this;
        }

        public OrderViewBuilder totalCents(long totalCents) {
            this.totalCents = totalCents;
            return this;
        }

        public OrderViewBuilder customerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
            return this;
        }

        public OrderViewBuilder addItem(String productSku, int quantity, long priceCents) {
            this.items.add(new Item(productSku, quantity, priceCents));
            return this;
        }

        public OrderViewBuilder addItem(Item item) {
            this.items.add(item);
            return this;
        }

        public OrderView build() {
            return new OrderView(id, status, totalCents, customerEmail, java.util.List.copyOf(items));
        }
    }

}