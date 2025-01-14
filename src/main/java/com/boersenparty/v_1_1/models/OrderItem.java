package com.boersenparty.v_1_1.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name="tOrderItems")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="order_id", nullable = false)
    @JsonBackReference
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="product_id", nullable = false)
    @JsonIgnore
    private Product product;
    private Integer quantity;

    private Double pricePerItem;

    private Double totalItemPrice;

    public Double getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(Double totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    public OrderItem() {
    }

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
                ", order=" + "wont show this hehe" +
                ", product=" + product +
                ", quantity=" + quantity +
                ", pricePerItem=" + pricePerItem +
                ", totalItemPrice=" + totalItemPrice +
                '}';
    }
}
