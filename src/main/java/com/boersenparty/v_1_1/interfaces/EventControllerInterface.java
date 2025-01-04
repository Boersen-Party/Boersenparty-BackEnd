package com.boersenparty.v_1_1.interfaces;


import com.boersenparty.v_1_1.dto.EventDTO;
import com.boersenparty.v_1_1.dto.ProductDTO;
import com.boersenparty.v_1_1.models.Event;
import com.boersenparty.v_1_1.models.Party;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping(path="/parties")
public interface EventControllerInterface {
    @GetMapping(path="/{party_id}/events")
    public List<EventDTO> getEvents(@PathVariable Long party_id);

    //@GetMapping(path="/{party_id}/events/{event_id}")
    //public Optional<Party> getEvent(@PathVariable Long party_id, @PathVariable Long event_id);


    //DELETE ALL EVENTS
    @DeleteMapping(path="/{party_id}/events")
    public ResponseEntity<String> deleteEvents(@PathVariable Long party_id);

    @PostMapping(path="/{party_id}/events")
    public ResponseEntity<EventDTO> createEvent(@RequestBody(required = true) EventDTO eventDTO, @PathVariable Long party_id);




   /*       just rely on deletion and creating new event
    @PutMapping(path="/{party_id}/events/{event_id}")
    public ResponseEntity<Event> updateEvent(
            @RequestBody(required = true) Party party,
            @PathVariable Long party_id,
            @PathVariable Long event_id);


    */

}
