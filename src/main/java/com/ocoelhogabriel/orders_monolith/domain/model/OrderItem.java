package com.ocoelhogabriel.orders_monolith.domain.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Min(1)
    private int quantity;

    @Column(name = "price_cents", nullable = false)
    @Min(0)
    private long priceCents;

    public OrderItem() {
        super();
    }

    public OrderItem(UUID id, Order order, long priceCents, Product product, int quantity) {
        this.id = id;
        this.order = order;
        this.priceCents = priceCents;
        this.product = product;
        this.quantity = quantity;
    }

    @PrePersist
    protected void prePersist() {
        if (id == null)
            id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getPriceCents() {
        return priceCents;
    }

    public void setPriceCents(long priceCents) {
        this.priceCents = priceCents;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OrderItem{");
        sb.append("id=").append(id);
        sb.append(", order=").append(order);
        sb.append(", product=").append(product);
        sb.append(", quantity=").append(quantity);
        sb.append(", priceCents=").append(priceCents);
        sb.append('}');
        return sb.toString();
    }

}
