package com.boersenparty.v_1_1.dto;

public class ProductDTO {
    private Long id;
    private String name;
    private Integer pQuantity;
    private Double price_base;
    private Double price_min;
    private Double price_max;
    private boolean is_active;
    private String imageURL;
    private String productType;


    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pQuantity=" + pQuantity +
                ", price_base=" + price_base +
                ", price_min=" + price_min +
                ", price_max=" + price_max +
                ", is_active=" + is_active +
                ", imageURL='" + imageURL + '\'' +
                ", productType='" + productType + '\'' +
                '}';
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
