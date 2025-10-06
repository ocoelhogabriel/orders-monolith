package com.ocoelhogabriel.orders_monolith.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocoelhogabriel.orders_monolith.domain.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {}
