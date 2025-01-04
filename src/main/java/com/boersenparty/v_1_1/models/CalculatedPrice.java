package com.boersenparty.v_1_1.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="tCalculatedPrice")
public class CalculatedPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime time;

    @ManyToOne()
    @JoinColumn(name = "product_id", nullable = false) // Foreign key to Product
    private Product product;

    private double price;

    public Long getId() {
        return id;
    }


    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public CalculatedPrice(){
        this.time = LocalDateTime.now();
    }

    public CalculatedPrice(Product product, Double price_base) {
        System.out.println("CalculatedPrice constructor called!");
        this.product = product;
        this.price = price_base;
        this.time = LocalDateTime.now();  // Store the current time
    }

    @Override
    public String toString() {
        return "CalculatedPrice{" +
                "id=" + id +
                ", time=" + time +
                ", product=" + product +
                ", price=" + price +
                '}';
    }
}
