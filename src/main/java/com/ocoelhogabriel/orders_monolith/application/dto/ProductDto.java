package com.ocoelhogabriel.orders_monolith.application.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@JacksonXmlRootElement(localName = "product")
@Schema(name = "Product", description = "DTO que representa um produto")
public record ProductDto(
                @NotBlank @Schema(description = "Nome do produto", example = "Camiseta Básica") String name,
                @NotBlank @Schema(description = "SKU do produto", example = "SKU-12345") String sku,
                @Min(0) @Schema(description = "Preço em centavos", example = "1999", minimum = "0") long priceCents) {
}