package com.ocoelhogabriel.orders_monolith.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocoelhogabriel.orders_monolith.domain.model.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findBySku(String sku);
}
