package com.boersenparty.v_1_1.models;

import jakarta.persistence.*;

@Entity
@Table(name="tOrderItems")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="order_id", nullable = true)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="product_id", nullable = true)
    private Product product;
    private Integer quantity;

    // TODO: priceSum abfrage Funktion
    private Double pricePerItem;
}
