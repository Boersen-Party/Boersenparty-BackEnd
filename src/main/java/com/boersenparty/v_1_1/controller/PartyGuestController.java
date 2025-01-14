package com.boersenparty.v_1_1.controller;



import com.boersenparty.v_1_1.interfaces.PartyGuestControllerInterface;
import com.boersenparty.v_1_1.models.PartyGuest;
import com.boersenparty.v_1_1.service.PartyGuestService;
import com.boersenparty.v_1_1.service.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PartyGuestController implements PartyGuestControllerInterface {

    @Autowired
    private final PartyGuestService partyGuestService;

    @Autowired
    private final PartyService partyService;
    public PartyGuestController(PartyGuestService partyGuestService, PartyService partyService ) {

        this.partyGuestService = partyGuestService;
        this.partyService = partyService;
    }

    @Override
    public List<PartyGuest> getGuests(Long party_id){
        return partyGuestService.getPartyGuests(party_id);
    }

    @Override
    public Optional<PartyGuest> getGuest(Long party_id, Long guest_id){
        return partyGuestService.getPartyGuestInParty(party_id, guest_id);
    }

    @Override
    public void deleteGuest(Long party_id, Long guest_id){
        partyGuestService.deleteGuestFromParty(party_id, guest_id);
    }

    @Override
    public ResponseEntity<PartyGuest> createGuest(PartyGuest guest, Long party_id){
        return partyGuestService.createGuest(guest, party_id);

    }
    @PutMapping(path="/{party_id}/guests/{guest_id}")
    public ResponseEntity<PartyGuest> updateGuest(PartyGuest guest, Long party_id, Long guest_id){
        return partyGuestService.updateGuest(guest, party_id, guest_id);
    }


}
