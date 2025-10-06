package com.ocoelhogabriel.orders_monolith.interfaces.http;

import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.ocoelhogabriel.orders_monolith.application.dto.ProductDto;
import com.ocoelhogabriel.orders_monolith.domain.model.Product;
import com.ocoelhogabriel.orders_monolith.domain.repository.ProductRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductRepository repo;

    public ProductController(ProductRepository repo) {
        this.repo = repo;
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<Object> create(@RequestBody @Valid ProductDto dto) {
        try {
            var p = new Product();
            p.setName(dto.name());
            p.setSku(dto.sku());
            p.setPriceCents(dto.priceCents());
            var saved = repo.save(p);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao criar produto: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable UUID id) {
        return repo.findById(id)
            .map(prod -> ResponseEntity.ok((Object) prod))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}