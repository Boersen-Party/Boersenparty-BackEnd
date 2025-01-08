package com.boersenparty.v_1_1.dto;
import java.util.UUID;

public class JoinPartyRequest {
    private String accessCode;

    private UUID uuid;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
}

