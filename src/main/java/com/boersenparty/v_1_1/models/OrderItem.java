package com.boersenparty.v_1_1.models;

import jakarta.persistence.*;

@Entity
@Table(name="tOrderItems")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="product_id", nullable = false)
    private Product product;
    private Integer quantity;

    // TODO: priceSum abfrage Funktion
    private Double pricePerItem;

    public OrderItem() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPricePerItem() {
        return pricePerItem;
    }

    public void setPricePerItem(Double pricePerItem) {
        this.pricePerItem = pricePerItem;
    }

    public Double calculateTotalPrice() {
        return quantity * pricePerItem;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", product=" + product +
                ", quantity=" + quantity +
                ", pricePerItem=" + pricePerItem +
                '}';
    }
}
