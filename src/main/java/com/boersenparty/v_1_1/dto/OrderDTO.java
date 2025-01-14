package com.boersenparty.v_1_1.dto;


import java.util.List;

public class OrderDTO {
    private Long id;
    private String belongs_to;
    private List<OrderItemDTO> items;
    private double totalPrice;
    private boolean isPaid;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBelongs_to() {
        return belongs_to;
    }

    public void setBelongs_to(String belongs_to) {
        this.belongs_to = belongs_to;
    }

    public  List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "belongs_to='" + belongs_to + '\'' +
                ", items=" + items +
                ", totalPrice=" + totalPrice +
                ", isPaid=" + isPaid +
                '}';
    }
}

