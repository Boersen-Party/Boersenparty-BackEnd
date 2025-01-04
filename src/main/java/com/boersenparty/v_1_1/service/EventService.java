package com.boersenparty.v_1_1.service;

import com.boersenparty.v_1_1.dto.EventDTO;
import com.boersenparty.v_1_1.mapper.EventDTOMapper;
import com.boersenparty.v_1_1.models.Event;
import com.boersenparty.v_1_1.models.Party;
import com.boersenparty.v_1_1.repository.EventRepository;
import com.boersenparty.v_1_1.repository.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private final PartyRepository partyRepository;
    @Autowired
    private final EventRepository eventRepository;

    public EventService(PartyRepository partyRepository, EventRepository eventRepository) {
        this.partyRepository = partyRepository;
        this.eventRepository = eventRepository;
    }


    //  TODO: consistent naming of 'party_id' throughout controllers, or 'product_id'
    public EventDTO createEvent(EventDTO eventDTO, Long party_id) {
        Party party = partyRepository.findById(party_id)
                .orElseThrow(() -> new IllegalArgumentException("Party not found with ID: " + party_id));

        Event event = new Event();
        event.setType(eventDTO.getType());
        event.setDuration(eventDTO.getDuration());
        event.setParty(party);
        event = eventRepository.save(event);

        return EventDTOMapper.toDTO(event);
    }

    public List<EventDTO> getEvents(Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new IllegalArgumentException("Party not found with ID: " + partyId));

        List<Event> events = eventRepository.findByParty(party);

        return EventDTOMapper.toDTOList(events);
    }

    public void deleteEvents(Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new IllegalArgumentException("Party not found with ID: " + partyId));

        List<Event> events = eventRepository.findByParty(party);

        eventRepository.deleteAll(events);
    }
}
