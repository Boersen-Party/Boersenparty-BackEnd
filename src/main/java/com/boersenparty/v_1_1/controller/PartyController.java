package com.boersenparty.v_1_1.controller;

import com.boersenparty.v_1_1.models.Party;
import com.boersenparty.v_1_1.service.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path="api/v1")
public class PartyController {
    @Autowired
    private final PartyService partyService;

    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    @GetMapping(path="/parties")
    public List<Party> getParties(){
        return partyService.getParties();
    }

    @PostMapping(path = "/party")
    public ResponseEntity<Party> createParty(@RequestBody(required = false) Party party) {
        if (party == null) {
            party = new Party();
        }
        return ResponseEntity.ok(partyService.createParty(party));
    }

    //This is the only place where PartyGuests are created
    @PutMapping(path="/party/join/{party_id}")
    public void joinParty(@PathVariable Long party_id) {
        partyService.joinParty(party_id);
    }
}
