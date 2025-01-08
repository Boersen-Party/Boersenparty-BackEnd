package com.boersenparty.v_1_1.dto;

import java.util.UUID;

public class JoinPartyResponse {
    private Long party_id;
    private UUID uuid;

    public Long getParty_id() {
        return party_id;
    }

    public void setParty_id(Long party_id) {
        this.party_id = party_id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
