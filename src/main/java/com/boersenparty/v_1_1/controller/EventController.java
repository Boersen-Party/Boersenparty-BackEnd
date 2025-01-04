package com.boersenparty.v_1_1.controller;

import com.boersenparty.v_1_1.dto.EventDTO;
import com.boersenparty.v_1_1.interfaces.EventControllerInterface;
import com.boersenparty.v_1_1.models.Event;
import com.boersenparty.v_1_1.models.Party;
import com.boersenparty.v_1_1.service.EventService;
import com.boersenparty.v_1_1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class EventController implements EventControllerInterface {

    @Autowired
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public List<EventDTO> getEvents(Long party_id){
        return eventService.getEvents(party_id);
    }


    @Override
    public ResponseEntity<String> deleteEvents(Long party_id){
        eventService.deleteEvents(party_id);
        return ResponseEntity.ok("Events of party with ID: " + party_id + " deleted successfully");
    }

    @Override
    public ResponseEntity<EventDTO> createEvent (EventDTO eventDTO, Long party_id){
        return ResponseEntity.ok(eventService.createEvent(eventDTO, party_id));
    }


}
