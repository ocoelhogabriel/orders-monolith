package com.ocoelhogabriel.orders_monolith.application.dto;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@JacksonXmlRootElement(localName = "order")
@Schema(name = "CreateOrder", description = "DTO usado para criar um pedido")
public record CreateOrderDto(
        @NotNull @Schema(description = "Email do cliente que fará o pedido", example = "joao@example.com", format = "email") String customerEmail,
        @NotEmpty @Schema(description = "Lista de itens do pedido", implementation = OrderItemDto.class) List<@NotNull OrderItemDto> items) {
}
