package com.boersenparty.v_1_1.mapper;

import com.boersenparty.v_1_1.dto.EventDTO;
import com.boersenparty.v_1_1.models.Event;

public class EventDTOMapper {

    public static EventDTO toDTO(Event event) {
        if (event == null) {
            return null;
        }

        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(event.getId());
        eventDTO.setType(event.getType());
        eventDTO.setDuration(event.getDuration());

        return eventDTO;
    }

    // Convert EventDTO to Event entity
    public static Event toEntity(EventDTO eventDTO) {
        if (eventDTO == null) {
            return null;
        }

        Event event = new Event();

        //event.setId(eventDTO.getId()); // id should be created in the model
        event.setType(eventDTO.getType());
        event.setDuration(eventDTO.getDuration());

        return event;
    }
}
