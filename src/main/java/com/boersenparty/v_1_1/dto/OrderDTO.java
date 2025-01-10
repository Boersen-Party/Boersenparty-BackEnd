package com.boersenparty.v_1_1.dto;


import java.util.List;

public class OrderDTO {
    private String belongs_to; // PartyGuest UUID
    private List<OrderItemDTO> items; // List of OrderItems
    private double totalPrice; // Total price of the order
    private boolean isPaid; // Payment status

    // Getters and Setters


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

