package com.boersenparty.v_1_1.service;

import com.boersenparty.v_1_1.models.Party;
import com.boersenparty.v_1_1.models.PartyGuest;
import com.boersenparty.v_1_1.repository.PartyGuestRepository;
import com.boersenparty.v_1_1.repository.PartyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartyService {
    private final PartyRepository partyRepository;
    private final PartyGuestRepository partyGuestRepository;

    public PartyService(PartyRepository partyRepository, PartyGuestRepository partyGuestRepository) {
        this.partyRepository = partyRepository;
        this.partyGuestRepository = partyGuestRepository;
    }

    public List<Party> getParties() {
        return partyRepository.findAll();
    }


    public Party createParty(Party party) {
        // if you have ze berechtigung, kannste parties mache ohne Ende
        return partyRepository.save(party);

    }

    public void joinParty(Long partyId) {
            Optional<Party> optionalParty = partyRepository.findById(partyId);
            if (optionalParty.isPresent()){
                PartyGuest partyGuest = new PartyGuest(optionalParty.get());
                partyGuestRepository.save(partyGuest);
            }
            else {
                throw new RuntimeException("Party not found with ID: " + partyId);
            }
        }
    }

