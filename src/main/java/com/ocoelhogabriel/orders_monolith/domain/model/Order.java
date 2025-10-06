package com.ocoelhogabriel.orders_monolith.domain.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "orders")
public class Order {

    public enum Status {
        CREATED, PAID, CANCELED
    }

    @Id
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "total_cents", nullable = false)
    @Min(0)
    private long totalCents;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @SuppressWarnings("FieldMayBeFinal")
    private List<OrderItem> items = new ArrayList<>();

    @PrePersist
    protected void prePersist() {
        if (id == null)
            id = UUID.randomUUID();
        if (createdAt == null)
            createdAt = OffsetDateTime.now();
        if (status == null)
            status = Status.CREATED;
    }

    public void setItems(List<OrderItem> newItems) {
        items.clear();
        if (newItems != null) {
            newItems.forEach(i -> {
                i.setOrder(this);
                items.add(i);
            });
        }
        recalcTotal();
    }

    public void recalcTotal() {
        totalCents = items.stream().mapToLong(i -> i.getPriceCents() * i.getQuantity()).sum();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getTotalCents() {
        return totalCents;
    }

    public void setTotalCents(long totalCents) {
        this.totalCents = totalCents;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public Order() {
        super();
    }

    public Order(OffsetDateTime createdAt, Customer customer, UUID id, Status status, long totalCents) {
        this.createdAt = createdAt;
        this.customer = customer;
        this.id = id;
        this.status = status;
        this.totalCents = totalCents;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order{");
        sb.append("id=").append(id);
        sb.append(", customer=").append(customer);
        sb.append(", status=").append(status);
        sb.append(", totalCents=").append(totalCents);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", items=").append(items);
        sb.append('}');
        return sb.toString();
    }

}
