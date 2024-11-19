package com.boersenparty.v_1_1.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


import java.util.List;
import java.util.UUID;


@Entity
@Table(name= "tPartyguests")
public class PartyGuest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private final UUID uuid;


    @OneToMany(mappedBy = "partyGuest", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Order> orders;

    @Override
    public String toString() {
        return "PartyGuest{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", orders=" + orders +
                ", party=" + party +
                '}';
    }

    @NotNull
    @ManyToOne
    @JoinColumn(name="party_id", nullable = false)
    @JsonIgnore
    private Party party;


    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public PartyGuest(){
        System.out.println("Default Guest constructor is called");
        this.uuid = UUID.randomUUID();
    }

    public PartyGuest(Party party){
        this.party = party;
        this.uuid = UUID.randomUUID();
        System.out.println("Second Guest constructor is called");

    }



    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }


    public void addOrder(Order order) {
        this.orders.add(order);
    }
}
