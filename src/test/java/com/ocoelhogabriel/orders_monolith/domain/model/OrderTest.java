package com.ocoelhogabriel.orders_monolith.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class OrderTest {
    @Test
    void recalcTotalSomandoItens() {
        var o = new Order();
        var p = new Product();
        p.setPriceCents(900);
        var i1 = new OrderItem();
        i1.setProduct(p);
        i1.setQuantity(2);
        i1.setPriceCents(900);
        var i2 = new OrderItem();
        i2.setProduct(p);
        i2.setQuantity(1);
        i2.setPriceCents(900);
        o.setItems(java.util.List.of(i1, i2));
        assertEquals(2700, o.getTotalCents());
    }
}