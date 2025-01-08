package com.boersenparty.v_1_1.dto;


public class OrderItemDTO {
    private Long productId; // ID of the product
    private String productName; // Name of the product
    private Double pricePerItem; // Price per item
    private Integer quantity; // Quantity of the product
    private Double totalItemPrice; // Total price for this item (quantity * price)

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPricePerItem() {
        return pricePerItem;
    }

    public void setPricePerItem(Double pricePerItem) {
        this.pricePerItem = pricePerItem;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(Double totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    @Override
    public String toString() {
        return "OrderItemDTO{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", pricePerItem=" + pricePerItem +
                ", quantity=" + quantity +
                ", totalItemPrice=" + totalItemPrice +
                '}';
    }
}

