package com.boersenparty.v_1_1.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ProductDTO {
    private Long id;
    private String name;
    private Integer pQuantity;
    private Double price_min;
    private Double price_max;
    private boolean is_active;
    private String imageURL;
    private String productType;
    private Double latestCalculatedPrice;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime PriceUpdatedAt;

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pQuantity=" + pQuantity +
                ", price_min=" + price_min +
                ", price_max=" + price_max +
                ", is_active=" + is_active +
                ", imageURL='" + imageURL + '\'' +
                ", productType='" + productType + '\'' +
                ", latestCalculatedPrice=" + latestCalculatedPrice +
                ", PriceUpdatedAt=" + PriceUpdatedAt +
                '}';
    }

    public LocalDateTime getPriceUpdatedAt() {
        return PriceUpdatedAt;
    }

    public void setPriceUpdatedAt(LocalDateTime priceUpdatedAt) {
        PriceUpdatedAt = priceUpdatedAt;
    }

    public Double getLatestCalculatedPrice() {
        return latestCalculatedPrice;
    }

    public void setLatestCalculatedPrice(Double latestCalculatedPrice) {
        this.latestCalculatedPrice = latestCalculatedPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


}
