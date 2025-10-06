package com.ocoelhogabriel.orders_monolith.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ocoelhogabriel.orders_monolith.domain.model.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    @EntityGraph(attributePaths = { "items", "items.product", "customer" })
    Optional<Order> findWithItemsById(UUID id);
}