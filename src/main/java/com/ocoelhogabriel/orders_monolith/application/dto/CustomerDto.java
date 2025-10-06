package com.ocoelhogabriel.orders_monolith.application.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@JacksonXmlRootElement(localName = "customer")
@Schema(name = "Customer", description = "DTO que representa um cliente")
public record CustomerDto(
        @NotBlank @Schema(description = "Nome do cliente", example = "João Silva") String name,
        @NotBlank @Email @Schema(description = "Email do cliente", example = "joao@example.com", format = "email") String email) {
}
