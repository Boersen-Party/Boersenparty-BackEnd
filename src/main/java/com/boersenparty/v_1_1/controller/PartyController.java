package com.boersenparty.v_1_1.controller;
import com.boersenparty.v_1_1.interfaces.PartyControllerInterface;
import com.boersenparty.v_1_1.models.Party;
import com.boersenparty.v_1_1.service.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PartyController implements PartyControllerInterface {
    @Autowired
    private final PartyService partyService;

    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }


    // Gets the KeyCloak UserID ("preffered username") from the Token
    private String getUserID() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof org.springframework.security.oauth2.jwt.Jwt) {
            org.springframework.security.oauth2.jwt.Jwt jwt = (org.springframework.security.oauth2.jwt.Jwt) principal;

            String name = jwt.getClaim("preferred_username");
            if (name != null) {
                return name;
            }
        }
        // Fallback
        System.out.println("ERROR: claim (PREFFERED USERNAME) unaccesible, returning getName() instead");
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    //@PreAuthorize("hasAuthority('PARTIES_READ')")
    public List<Party> getParties() {
        System.out.println("getParties() called");
        return partyService.getPartiesHostedBy(getUserID());
    }

    @Override
    public Optional<Party> getParty(Long party_id) {
        return partyService.getParty(party_id);
    }

    @Override
    public void deleteParty(Long party_id) {
        partyService.deleteParty(party_id);
    }



    @Override
    @PreAuthorize("hasAuthority('PARTIES_CREATE')")
    public ResponseEntity<Party> createParty(Party party) {
        System.out.println("create Party called");
        System.out.println("party about to be created is:" + party);
        return ResponseEntity.ok(partyService.createParty(party));
    }

    @Override
    public ResponseEntity<Party> updateParty(Party party,Long party_id) {
        return partyService.updateParty(party, party_id);
    }






}
