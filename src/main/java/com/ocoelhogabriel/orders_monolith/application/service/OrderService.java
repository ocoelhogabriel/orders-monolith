package com.ocoelhogabriel.orders_monolith.application.service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocoelhogabriel.orders_monolith.application.dto.CreateOrderDto;
import com.ocoelhogabriel.orders_monolith.application.event.PedidoCriadoEvent;
import com.ocoelhogabriel.orders_monolith.domain.model.Order;
import com.ocoelhogabriel.orders_monolith.domain.model.OrderItem;
import com.ocoelhogabriel.orders_monolith.domain.repository.CustomerRepository;
import com.ocoelhogabriel.orders_monolith.domain.repository.OrderRepository;
import com.ocoelhogabriel.orders_monolith.domain.repository.ProductRepository;
import com.ocoelhogabriel.orders_monolith.infrastructure.messaging.PedidoProducer;
import com.ocoelhogabriel.orders_monolith.infrastructure.messaging.Topics;

@Service
public class OrderService {

    private final CustomerRepository customers;
    private final ProductRepository products;
    private final OrderRepository orders;
    private final PedidoProducer producer;
    private final String topicPedidoCriado;

    public OrderService(CustomerRepository customers, ProductRepository products,
            OrderRepository orders, PedidoProducer producer,
            @Value(Topics.PEDIDO_CRIADO) String topicPedidoCriado) {
        this.customers = customers;
        this.products = products;
        this.orders = orders;
        this.producer = producer;
        this.topicPedidoCriado = topicPedidoCriado;
    }

    @Transactional
    public Order create(CreateOrderDto dto) {
        // Guard Clauses
        var customer = customers.findAll().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(dto.customerEmail()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + dto.customerEmail()));

        var order = new Order();
        order.setCustomer(customer);

        var items = new ArrayList<OrderItem>();
        for (var it : dto.items()) {
            var product = products.findBySku(it.productSku())
                    .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + it.productSku()));
            var oi = new OrderItem();
            oi.setProduct(product);
            oi.setQuantity(it.quantity());
            oi.setPriceCents(product.getPriceCents());
            items.add(oi);
        }
        order.setItems(items);
        order.recalcTotal();

        var saved = orders.save(order);

        // Evento (eventual consistency para outros domínios)
        var event = new PedidoCriadoEvent(
                saved.getId().toString(),
                customer.getEmail(),
                saved.getTotalCents(),
                saved.getItems().stream()
                        .map(i -> new PedidoCriadoEvent.Item(i.getProduct().getSku(), i.getQuantity(),
                                i.getPriceCents()))
                        .toList());
        producer.publish(event, topicPedidoCriado);

        return saved;
    }

    @Transactional(readOnly = true)
    public Optional<Order> get(UUID id) {
        return orders.findWithItemsById(id);
    }
}