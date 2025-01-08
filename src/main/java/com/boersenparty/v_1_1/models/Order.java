package com.boersenparty.v_1_1.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name="tOrders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partyguest_id", nullable = false)
    @JsonBackReference
    private PartyGuest partyGuest;

    public PartyGuest getPartyGuest() {
        return partyGuest;
    }

    public void setPartyGuest(PartyGuest partyGuest) {
        this.partyGuest = partyGuest;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    private boolean is_paid = false;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yy HH:mm:ss")
    private LocalDateTime expires_at;

    public Order() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public boolean isIs_paid() {
        return is_paid;
    }

    public void setIs_paid(boolean is_paid) {
        this.is_paid = is_paid;
    }

    public LocalDateTime getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(LocalDateTime expires_at) {
        this.expires_at = expires_at;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", partyGuest=" + partyGuest +
                ", party=" + party +
                ", orderItems=" + orderItems +
                ", is_paid=" + is_paid +
                ", expires_at=" + expires_at +
                '}';
    }
}
