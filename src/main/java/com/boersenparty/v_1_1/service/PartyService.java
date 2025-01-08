package com.boersenparty.v_1_1.service;

import com.boersenparty.v_1_1.dto.JoinPartyResponse;
import com.boersenparty.v_1_1.models.Party;
import com.boersenparty.v_1_1.models.PartyGuest;
import com.boersenparty.v_1_1.repository.PartyGuestRepository;
import com.boersenparty.v_1_1.repository.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.boersenparty.v_1_1.utils.TokenUtils.*;

// TODO: "n_guests" als Abfrage funktion in PartyService

@Service
public class PartyService {
    private final PartyRepository partyRepository;
    private final PartyGuestRepository partyGuestRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public PartyService(PartyRepository partyRepository, PartyGuestRepository partyGuestRepository) {
        this.partyRepository = partyRepository;
        this.partyGuestRepository = partyGuestRepository;
    }
    public List<Party> getPartiesHostedBy(String userId) {
        System.out.println("keycloak userid is:" + userId);
        System.out.println(partyRepository.findByHostedBy(userId));
        return partyRepository.findByHostedBy(userId);
    }
    public Long checkUUIDValidity(UUID uuid) {
        Optional<PartyGuest> partyGuest = partyGuestRepository.findByUuid(uuid);
        return partyGuest.map(guest -> guest.getParty().getId()).orElse(null);  // Return partyId or null if not found
    }

    public List<Party> getParties() {
        return partyRepository.findAll();
    }

    public Long getPartyIdByHost(String worksFor) {
        List<Party> parties = partyRepository.findByHostedBy(worksFor);
        if (parties.isEmpty()) {
            return null;
        }
        return parties.get(0).getId();
    }


    public Party createParty(Party party) {
        if (party.getName() == null) {
            throw new IllegalArgumentException("Party name cannot be null");
        }

        String accessCode = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        party.setAccessCode(accessCode);
        Party savedParty = partyRepository.save(party);

        return savedParty;
    }


    //
    public ResponseEntity<JoinPartyResponse> joinParty(String accessCode) {

        System.out.println("Calling join Party");
        Optional<Party> optionalParty = partyRepository.findByAccessCode(accessCode);

        if (optionalParty.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        Party party = optionalParty.get();
        System.out.println("Gotten Party is:" + party);

        PartyGuest partyGuest = new PartyGuest(party);
        PartyGuest savedGuest = partyGuestRepository.save(partyGuest);

        System.out.println("Returning OK ");
        JoinPartyResponse response = new JoinPartyResponse();
        response.setUuid(UUID.fromString(savedGuest.getUuid()));
        response.setParty_id(party.getId());
        return ResponseEntity.ok(response);
    }


    public Optional <Party> getParty(Long partyId) {
        Optional<Party> optionalParty = partyRepository.findById(partyId);
        if (optionalParty.isPresent()) {
            return optionalParty;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Party with id: " + partyId + " not found");
        }
    }


    public void deleteParty(Long partyId) {
        this.partyRepository.deleteById(partyId);
    }

    public ResponseEntity<Party> updateParty(Party party, Long party_id) {
        Party existingParty = partyRepository.findById(party_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid party ID - Party not found"));

        existingParty.setName(party.getName());
        existingParty.setPartyGuests(party.getPartyGuests());
        existingParty.setHosted_by(party.getHosted_by());
        existingParty.setStart_date(party.getStart_date());
        existingParty.setEnd_date(party.getEnd_date());

        partyRepository.save(existingParty);
        System.out.println("Party: " + existingParty + "has been updated");

        return ResponseEntity.ok(existingParty);
    }


    public List<Party> getAccessibleParties() {
        String userId = getUserID(); // Retrieve the user ID from the token
        System.out.println("keycloak userid is:" + userId);

        if (hasAuthority("_VERANSTALTER")) {
            System.out.println("Authority: _VERANSTALTER!!!!!!!!!!");
            return getPartiesHostedBy(userId);
        } else if (hasAuthority("_PERSONAL")) {
            System.out.println("Authority: _PERSONAL!!!!!!!!!!");

            String veranstalterId = getWorksForFromToken(); // Retrieve the associated Veranstalter ID
            System.out.println("works for is:" + veranstalterId);

            if (veranstalterId == null || veranstalterId.isEmpty()) {
                throw new AccessDeniedException("Personal user is not associated with any Veranstalter.");
            }

            return getPartiesHostedBy(veranstalterId);
        } else {
            throw new AccessDeniedException("User does not have access to any parties.");
        }
    }



}

