package com.boersenparty.v_1_1.controller;
import com.boersenparty.v_1_1.interfaces.PartyControllerInterface;
import com.boersenparty.v_1_1.models.Party;
import com.boersenparty.v_1_1.service.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class PartyController implements PartyControllerInterface {
    @Autowired
    private final PartyService partyService;

    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    @Override
    //@PreAuthorize("hasAuthority('PARTIES_READ')")
    public List<Party> getParties() {
        System.out.println("getParties() called");
        return partyService.getParties();
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
        return ResponseEntity.ok(partyService.createParty(party));
    }

    // Needs lots of testing - also implement Response Codes
    @Override
    public ResponseEntity<Party> updateParty(Party party,Long party_id) {
        return partyService.updateParty(party, party_id);
    }






}
