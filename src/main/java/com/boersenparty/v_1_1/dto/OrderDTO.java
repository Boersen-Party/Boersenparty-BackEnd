package com.boersenparty.v_1_1.dto;

public class OrderDTO {
    // TODO: adjust for OrderItems
    private Long partyguest_id;
    private Long party_id;

    public Long getPartyguest_id() {
        return partyguest_id;
    }

    public void setPartyguest_id(Long partyguest_id) {
        this.partyguest_id = partyguest_id;
    }

    public Long getParty_id() {
        return party_id;
    }

    public void setParty_id(Long party_id) {
        this.party_id = party_id;
    }
}
