package com.boersenparty.v_1_1.service;

import com.boersenparty.v_1_1.models.Party;
import com.boersenparty.v_1_1.models.PartyGuest;
import com.boersenparty.v_1_1.repository.PartyGuestRepository;
import com.boersenparty.v_1_1.repository.PartyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    public PartyGuest getPartyGuest(Long id) {
        return partyGuestRepository.findById(id).get();
    }

    public Optional <PartyGuest> getPartyGuestInParty(Long partyId, Long guestId) {


        Optional<Party> optionalParty = partyRepository.findById(partyId);
        if (optionalParty.isEmpty()){ throw new RuntimeException("Party with ID " + partyId + " not found"); }


        Optional<PartyGuest> optionalGuest = partyGuestRepository.findById(guestId);
        if (optionalGuest.isEmpty()){ throw new RuntimeException("Guest with ID " + partyId + " not found"); }

        PartyGuest partyGuest = optionalGuest.get();
        if (!partyGuest.getParty().getId().equals(partyId)) {
            throw new RuntimeException("PartyGuest with ID " + guestId + " does not belong to Party with ID " + partyId);
        }

        // Return the found PartyGuest
        return Optional.of(partyGuest);



    }

    public void deleteGuestFromParty(Long partyId, Long guestId) {
        //RuntimeException should be ResourceNotFound Exception or sth like that

        Optional<Party> optionalParty = partyRepository.findById(partyId);
        if (optionalParty.isEmpty()) {
            throw new RuntimeException("Party with ID " + partyId + " not found");
        }

        Optional<PartyGuest> optionalPartyGuest = partyGuestRepository.findById(guestId);
        if (optionalPartyGuest.isEmpty()) {
            throw new RuntimeException("PartyGuest with ID " + guestId + " not found");
        }

        PartyGuest partyGuest = optionalPartyGuest.get();
        if (!partyGuest.getParty().getId().equals(partyId)) {
            throw new IllegalArgumentException("PartyGuest with ID " + guestId + " does not belong to Party with ID " + partyId);
        }

        partyGuestRepository.delete(partyGuest);
    }

    public ResponseEntity<PartyGuest> createGuest(PartyGuest guest, Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new RuntimeException("Party with ID " + partyId + " not found"));

        guest.setParty(party);

        PartyGuest createdGuest = partyGuestRepository.save(guest);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdGuest);
    }

    public ResponseEntity<PartyGuest> updateGuest(PartyGuest guest, Long partyId, Long guestId) {
        Optional<Party> optionalParty = partyRepository.findById(partyId);
        if (optionalParty.isEmpty()) {
            throw new RuntimeException("Party with ID " + partyId + " not found");
        }

        Optional<PartyGuest> optionalPartyGuest = partyGuestRepository.findById(guestId);
        if (optionalPartyGuest.isEmpty()) {
            throw new RuntimeException("PartyGuest with ID " + guestId + " not found");
        }

        PartyGuest partyGuest = optionalPartyGuest.get();
        if (!partyGuest.getParty().getId().equals(partyId)) {
            throw new IllegalArgumentException("PartyGuest with ID " + guestId + " does not belong to Party with ID " + partyId);
        }

        PartyGuest updatedGuest = new PartyGuest(
                optionalParty.get()
        );

        PartyGuest savedGuest = partyGuestRepository.save(updatedGuest);

        return ResponseEntity.ok(savedGuest);
    }

}
