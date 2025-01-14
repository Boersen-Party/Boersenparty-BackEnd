package com.boersenparty.v_1_1;

import com.boersenparty.v_1_1.controller.EventController;
import com.boersenparty.v_1_1.dto.EventDTO;
import com.boersenparty.v_1_1.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class EventControllerTests {

    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    @Test
    void testGetEvents() throws Exception {
        Long partyId = 1L;
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(1L);
        eventDTO.setType("Test Event");
        when(eventService.getEvents(partyId)).thenReturn(Collections.singletonList(eventDTO));

        mockMvc.perform(get("/events/{party_id}", partyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventName").value("Test Event"));

        verify(eventService, times(1)).getEvents(partyId);
    }

    @Test
    void testDeleteEvents() throws Exception {
        Long partyId = 1L;
        doNothing().when(eventService).deleteEvents(partyId);

        mockMvc.perform(delete("/events/{party_id}", partyId))
                .andExpect(status().isOk())
                .andExpect(content().string("Events of party with ID: " + partyId + " deleted successfully"));

        verify(eventService, times(1)).deleteEvents(partyId);
    }

    @Test
    void testCreateEvent() throws Exception {
        Long partyId = 1L;
        EventDTO eventDTO = new EventDTO();
        eventDTO.setType("New Event");
        when(eventService.createEvent(any(EventDTO.class), eq(partyId))).thenReturn(eventDTO);

        mockMvc.perform(post("/events/{party_id}", partyId)
                        .contentType("application/json")
                        .content("{\"eventType\":\"New Event\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventType").value("New Event"));

        verify(eventService, times(1)).createEvent(any(EventDTO.class), eq(partyId));
    }

    @Test
    void testTriggerEvent() throws Exception {
        Long partyId = 1L;
        Long eventId = 1L;
        String triggeredEvent = "Event Triggered Successfully";
        when(eventService.triggerEvent(partyId, eventId)).thenReturn(triggeredEvent);

        mockMvc.perform(post("/events/{party_id}/{event_id}/trigger", partyId, eventId))
                .andExpect(status().isOk())
                .andExpect(content().string(triggeredEvent));

        verify(eventService, times(1)).triggerEvent(partyId, eventId);
    }
}
