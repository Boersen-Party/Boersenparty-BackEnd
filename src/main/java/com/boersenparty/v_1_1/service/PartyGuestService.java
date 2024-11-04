package com.boersenparty.v_1_1.service;

import com.boersenparty.v_1_1.models.Party;
import com.boersenparty.v_1_1.models.PartyGuest;
import com.boersenparty.v_1_1.repository.PartyGuestRepository;
import com.boersenparty.v_1_1.repository.PartyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartyGuestService {
    private final PartyGuestRepository partyGuestRepository;
    private final PartyRepository partyRepository;

    public PartyGuestService(PartyGuestRepository partyGuestRepository, PartyRepository partyRepository) {
        this.partyGuestRepository = partyGuestRepository;
        this.partyRepository = partyRepository;
    }

    public List<PartyGuest> getPartyGuests() {
        return partyGuestRepository.findAll();

    }


}
