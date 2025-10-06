package com.ocoelhogabriel.orders_monolith.application.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.ocoelhogabriel.orders_monolith.application.dto.OrderView;
import com.ocoelhogabriel.orders_monolith.domain.model.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "customerEmail", source = "customer.email")
    OrderView toView(Order order);

    @AfterMapping
    default void fillItems(Order order, @MappingTarget OrderView.OrderViewBuilder builder) {}
}