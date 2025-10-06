package com.ocoelhogabriel.orders_monolith.interfaces.http;

import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.ocoelhogabriel.orders_monolith.application.dto.CustomerDto;
import com.ocoelhogabriel.orders_monolith.domain.model.Customer;
import com.ocoelhogabriel.orders_monolith.domain.repository.CustomerRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerRepository repo;

    public CustomerController(CustomerRepository repo) {
        this.repo = repo;
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<Object> create(@RequestBody @Valid CustomerDto dto) {
        try {
            var c = new Customer();
            c.setName(dto.name());
            c.setEmail(dto.email());
            var saved = repo.save(c);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao criar cliente: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable UUID id) {
        return repo.findById(id)
            .map(cust -> ResponseEntity.ok((Object) cust))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}