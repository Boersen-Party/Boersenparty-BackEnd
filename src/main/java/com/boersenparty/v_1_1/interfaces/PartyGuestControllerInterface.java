package com.boersenparty.v_1_1.interfaces;
import com.boersenparty.v_1_1.models.PartyGuest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping(path="/parties")
public interface PartyGuestControllerInterface {

    @GetMapping(path="/{party_id}/guests")
    public List<PartyGuest> getGuests();

    @GetMapping(path="/{party_id}/guests/{guest_id}")
    public Optional<PartyGuest> getGuest(@PathVariable Long party_id, @PathVariable Long guest_id);

    @DeleteMapping(path="/{party_id}/guests/{guest_id}")
    public void deleteGuest(@PathVariable Long party_id, @PathVariable Long guest_id);

    @PostMapping(path="/{party_id}/guests")
    public ResponseEntity<PartyGuest> createGuest(@RequestBody(required = true) PartyGuest guest,
                                                  @PathVariable Long party_id);

    @PutMapping(path="/{party_id}/guests/{guest_id}")
    public ResponseEntity<PartyGuest> updateGuest(
            @RequestBody(required = true) PartyGuest guest,
            @PathVariable Long party_id,
            @PathVariable Long guest_id);
}
