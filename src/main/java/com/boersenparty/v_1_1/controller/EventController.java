package com.boersenparty.v_1_1.controller;

import com.boersenparty.v_1_1.interfaces.EventControllerInterface;
import com.boersenparty.v_1_1.models.Event;
import com.boersenparty.v_1_1.models.Party;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class EventController implements EventControllerInterface {
    @Override
    public List<Event> getEvents(Long party_id){
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public Optional<Party> getEvent( Long party_id, Long event_id){
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public void deleteEvent(Long party_id,  Long event_id){
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public ResponseEntity<Event> createEvent( Event event){
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public ResponseEntity<Event> updateEvent(Party party, Long party_id, Long event_id){
        throw new UnsupportedOperationException("Method not implemented yet");
    }
}
