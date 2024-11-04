package com.boersenparty.v_1_1.controller;



import com.boersenparty.v_1_1.models.PartyGuest;
import com.boersenparty.v_1_1.service.PartyGuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1")
public class PartyGuestController {
    @Autowired
    private final PartyGuestService partyGuestService;

    public PartyGuestController(PartyGuestService partyGuestService ) {
        this.partyGuestService = partyGuestService;
    }

    @GetMapping(path="/guests")
    public List<PartyGuest> getPartyGuests(){
        return partyGuestService.getPartyGuests();
    }



}
