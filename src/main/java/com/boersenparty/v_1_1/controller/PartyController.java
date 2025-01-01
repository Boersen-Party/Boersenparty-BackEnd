package com.boersenparty.v_1_1.controller;
import com.boersenparty.v_1_1.interfaces.PartyControllerInterface;
import com.boersenparty.v_1_1.models.Party;
import com.boersenparty.v_1_1.service.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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

    private boolean hasAuthority(String authority) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority));
    }

    private String getWorksForFromToken() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof org.springframework.security.oauth2.jwt.Jwt) {
            org.springframework.security.oauth2.jwt.Jwt jwt = (org.springframework.security.oauth2.jwt.Jwt) principal;
            return jwt.getClaim("works_for");
        }

        // Fallback if the attribute is missing or token is invalid
        return null;
    }



    /*
    Ein 'Veranstalter' braucht bloß die '_VERANSTALTER' Rolle um getParties() zu betätigen

    Ein 'Personaler' jedoch braucht die '_PERSONAL' Rolle UND das Custom Attribute 'works_for'
    muss in Keycloak gesetzt werden -> sodass ein entsprechendes Mapping zwischen den Arbeitern
    eines Veranstalters möglich ist
    z.b. 'works_for' = 'veranstalter' /
    'works_for' = 'veranstalter2'

     */

    @Override
    @PreAuthorize("hasAnyAuthority('_VERANSTALTER', '_PERSONAL')")
    public List<Party> getParties() {
        System.out.println("getParties is called in controller!");
        if (hasAuthority("_VERANSTALTER")) {
            System.out.println("Authority: _VERANSTALTER!!!!!!!!!!");
            return partyService.getPartiesHostedBy(getUserID());
        } else if (hasAuthority("_PERSONAL")) {
            System.out.println("Authority: _PERSONAL!!!!!!!!!!");

            String veranstalterId = getWorksForFromToken();
            System.out.println("works for is:" + veranstalterId);
            if (veranstalterId == null || veranstalterId.isEmpty()) {
                throw new AccessDeniedException("Personal user is not associated with any Veranstalter.");
            }

            return partyService.getPartiesHostedBy(veranstalterId);
        }

        else {
            throw new AccessDeniedException("User does not have access to any parties.");
        }

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
        System.out.println("party about to be created is:" + party);
        return ResponseEntity.ok(partyService.createParty(party));
    }

    @Override
    public ResponseEntity<Party> updateParty(Party party,Long party_id) {
        return partyService.updateParty(party, party_id);
    }






}
