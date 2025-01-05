package com.boersenparty.v_1_1.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="tProducts")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false) //probably should be optional = false
    @JoinColumn(name = "party_id", nullable = true)
    @JsonIgnore
    private Party party;

    private String name;

    private Integer pQuantity;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JsonIgnore
    private List<CalculatedPrice> calculatedPrices;
    private Double price_min;
    private Double price_max;

    private boolean is_active;

    private String imageURL;

    // TODO: productType table
    private String productType;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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


    public List<CalculatedPrice> getCalculatedPrices() {
        return calculatedPrices;
    }

    public void setCalculatedPrices(List<CalculatedPrice> calculatedPrices) {
        this.calculatedPrices = calculatedPrices;
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }



    public Product(Party party, String name, Integer pQuantity, List<CalculatedPrice> calculatedPrices, Double price_min, Double price_max, boolean is_active, String image, String productType) {
        this.party = party;
        this.name = name;
        this.pQuantity = pQuantity;
        this.calculatedPrices = calculatedPrices;
        this.price_min = price_min;
        this.price_max = price_max;
        this.is_active = (pQuantity > 0) ? true : false;
        this.imageURL = image;
        this.productType = productType;
    }

    public Product(){}

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", party=" + party +
                ", name='" + name + '\'' +
                ", pQuantity=" + pQuantity +
                //", calculatedPrices=" + calculatedPrices +
                ", price_min=" + price_min +
                ", price_max=" + price_max +
                ", is_active=" + is_active +
                ", imageURL='" + imageURL + '\'' +
                ", productType='" + productType + '\'' +
                '}';
    }
}
