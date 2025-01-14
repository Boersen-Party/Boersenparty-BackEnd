package com.boersenparty.v_1_1.mapper;
import com.boersenparty.v_1_1.dto.EventDTO;
import com.boersenparty.v_1_1.models.Event;
import java.util.List;
import java.util.stream.Collectors;

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

    public static List<EventDTO> toDTOList(List<Event> events) {
        return events.stream()
                .map(EventDTOMapper::toDTO)
                .collect(Collectors.toList());
    }
}
