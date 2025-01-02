package com.boersenparty.v_1_1.events;

import com.boersenparty.v_1_1.models.CalculatedPrice;

public class PriceUpdateEvent {

    private Long productId;
    private CalculatedPrice calculatedPrice;

    public PriceUpdateEvent(Long productId, CalculatedPrice calculatedPrice) {
        this.productId = productId;
        this.calculatedPrice = calculatedPrice;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public CalculatedPrice getCalculatedPrice() {
        return calculatedPrice;
    }

    public void setCalculatedPrice(CalculatedPrice calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
    }

    @Override
    public String toString() {
        return "PriceUpdateEvent{" +
                "productId=" + productId +
                ", calculatedPrice=" + calculatedPrice +
                '}';
    }
}

