package com.boersenparty.v_1_1.models;

import jakarta.persistence.*;

@Entity
@Table(name="tProducts")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "party_id", nullable = true)
    private Party party;

    private String name = "Bier Augustiner 0.5l ";


    private Integer pQuantity= 20;
    private Double price_base = 0.6;
    private Double price_min = 0.4;
    private Double price_max = 2.3;

    private boolean is_active=true; //is_in_stock

    private String image = "Here the image of a Product is shown!";

    // TODO: productType table
    private String productType = "beverage";

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getpQuantity() {
        return pQuantity;
    }

    public void setpQuantity(Integer pQuantity) {
        this.pQuantity = pQuantity;
    }

    public Double getPrice_base() {
        return price_base;
    }

    public void setPrice_base(Double price_base) {
        this.price_base = price_base;
    }

    public Double getPrice_min() {
        return price_min;
    }

    public void setPrice_min(Double price_min) {
        this.price_min = price_min;
    }

    public Double getPrice_max() {
        return price_max;
    }

    public void setPrice_max(Double price_max) {
        this.price_max = price_max;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Product(Party party, String name, Integer pQuantity, Double price_base, Double price_min, Double price_max, boolean is_active, String image, String productType) {
        this.party = party;
        this.name = name;
        this.pQuantity = pQuantity;
        this.price_base = price_base;
        this.price_min = price_min;
        this.price_max = price_max;
        this.is_active = is_active;
        this.image = image;
        this.productType = productType;
    }

    public Product(){}

    @Override
    public String toString() {
        return "Products{" +
                "id=" + id +
                ", party=" + party +
                ", name='" + name + '\'' +
                ", pQuantity=" + pQuantity +
                ", price_base=" + price_base +
                ", price_min=" + price_min +
                ", price_max=" + price_max +
                ", is_active=" + is_active +
                ", image='" + image + '\'' +
                ", productType='" + productType + '\'' +
                '}';
    }
}
