package com.boersenparty.v_1_1.models;

import jakarta.persistence.*;

import java.util.List;


// TODO: "n_guests" als Abfrage funktion in PartyService
@Entity
@Table(name="tParties")
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Possible Strings: ACTIVE: The party is currently ongoing and participants can buy or sell stocks.
    //
    //INACTIVE: The party is not currently active, and no transactions can take place.
    //
    //PENDING: The party is being set up or is awaiting the start time.
    private String status = "PENDING";

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartyGuest> partyGuests;


    // TODO this functionality
    private String partyHost = "Admin";

    public Party(){

    }

    public Party(String status){
        this.status = status;
    }

    public Long getId() {
        return id;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PartyGuest> getPartyGuests() {
        return partyGuests;
    }

    public void setPartyGuests(List<PartyGuest> partyGuests) {
        this.partyGuests = partyGuests;
    }

    public String getPartyhost() {
        return partyHost;
    }

    public void setPartyhost(String partyHost) {
        this.partyHost = partyHost;
    }
}
