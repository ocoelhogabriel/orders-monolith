package com.ocoelhogabriel.orders_monolith.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "OrderItem", description = "Item do pedido com SKU e quantidade")
public record OrderItemDto(
        @NotBlank @Schema(description = "SKU do produto", example = "SKU-12345") String productSku,
        @Min(1) @Schema(description = "Quantidade solicitada", example = "2", minimum = "1") int quantity) {
}
