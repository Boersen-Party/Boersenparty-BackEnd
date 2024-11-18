package com.boersenparty.v_1_1.interfaces;

import com.boersenparty.v_1_1.models.Party;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping(path="/parties")
public interface PartyControllerInterface {
    // CRUD Operations for Party

    @GetMapping
    public List<Party> getParties();

    @GetMapping(path="/{party_id}")
    public Optional <Party> getParty(@PathVariable Long party_id);

    @DeleteMapping(path="/{party_id}")
    public void deleteParty(@PathVariable Long party_id);

    @PostMapping
    public ResponseEntity<Party> createParty(@RequestBody(required = true) Party party);

    @PutMapping(path="/{party_id}")
    public ResponseEntity<Party> updateParty(
            @RequestBody(required = true) Party party,
            @PathVariable Long party_id);


}
