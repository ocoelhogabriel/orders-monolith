package com.ocoelhogabriel.orders_monolith.interfaces.http;

import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.ocoelhogabriel.orders_monolith.application.dto.CreateOrderDto;
import com.ocoelhogabriel.orders_monolith.application.dto.OrderView;
import com.ocoelhogabriel.orders_monolith.application.dto.OrderView.Item;
import com.ocoelhogabriel.orders_monolith.application.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<Object> create(@RequestBody @Valid CreateOrderDto dto) {
        try {
            var o = service.create(dto);
            var view = new OrderView(
                    o.getId().toString(),
                    o.getStatus().name(),
                    o.getTotalCents(),
                    o.getCustomer().getEmail(),
                    o.getItems().stream()
                            .map(oi -> new Item(oi.getProduct().getSku(), oi.getQuantity(), oi.getPriceCents()))
                            .toList());
            return ResponseEntity.ok(view);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao criar pedido: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable UUID id) {
        var opt = service.get(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        var o = opt.get();
        var view = new OrderView(
                o.getId().toString(),
                o.getStatus().name(),
                o.getTotalCents(),
                o.getCustomer().getEmail(),
                o.getItems().stream()
                        .map(oi -> new Item(oi.getProduct().getSku(), oi.getQuantity(), oi.getPriceCents()))
                        .toList());
        return ResponseEntity.ok(view);
    }

}
