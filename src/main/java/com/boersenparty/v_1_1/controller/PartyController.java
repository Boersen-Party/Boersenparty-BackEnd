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
        return partyService.getAccessibleParties();
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
    @PreAuthorize("hasAuthority('_VERANSTALTER')")
    public ResponseEntity<Party> createParty(Party party) {
        System.out.println("party about to be created is:" + party);
        return ResponseEntity.ok(partyService.createParty(party));
    }

    @Override
    public ResponseEntity<Party> updateParty(Party party,Long party_id) {
        return partyService.updateParty(party, party_id);
    }






}
