package com.boersenparty.v_1_1;

import com.boersenparty.v_1_1.controller.OrderController;
import com.boersenparty.v_1_1.dto.OrderDTO;
import com.boersenparty.v_1_1.mapper.OrderDTOMapper;
import com.boersenparty.v_1_1.models.Order;
import com.boersenparty.v_1_1.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class OrderControllerTests {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @Mock
    private OrderDTOMapper orderMapper;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void testGetOrders() throws Exception {
        // Given
        Long partyId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setBelongs_to("Test User");
        when(orderService.findOrdersByPartyId(partyId)).thenReturn(Collections.singletonList(new Order()));
        when(orderMapper.mapToOrderDTOList(anyList())).thenReturn(Collections.singletonList(orderDTO));

        // When & Then
        mockMvc.perform(get("/orders/{party_id}", partyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].belongs_to").value("Test User"));

        verify(orderService, times(1)).findOrdersByPartyId(partyId);
    }

    @Test
    void testGetUsersOrders() throws Exception {
        // Given
        String uuid = "user-uuid";
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setBelongs_to("User's Test UUID");
        when(orderService.getOrdersByPartyGuestUuid(uuid)).thenReturn(Collections.singletonList(new Order()));
        when(orderMapper.mapToOrderDTOList(anyList())).thenReturn(Collections.singletonList(orderDTO));

        // When & Then
        mockMvc.perform(get("/orders/user/{uuid}", uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].belongs_to").value("User's Test UUID"));

        verify(orderService, times(1)).getOrdersByPartyGuestUuid(uuid);
    }

    @Test
    void testCreateReservation() throws Exception {
        // Given
        Long partyId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBelongs_to("user_uuid");
        Order order = new Order();
        order.setId(1L);
        when(orderService.createReservation(eq(partyId), any(OrderDTO.class))).thenReturn(order);
        when(orderMapper.mapToOrderDTO(any(Order.class))).thenReturn(orderDTO);

        // When & Then
        mockMvc.perform(post("/orders/{party_id}/reserve", partyId)
                        .contentType("application/json")
                        .content("{\"belongs_to\":\"user_uuid\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.belongs_to").value("user_uuid"));

        verify(orderService, times(1)).createReservation(eq(partyId), any(OrderDTO.class));
    }

    @Test
    void testProcessOrderPayment() throws Exception {
        // Given
        Long partyId = 1L;
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        when(orderService.processOrderPayment(partyId, orderId)).thenReturn(order);

        // When & Then
        mockMvc.perform(post("/orders/{party_id}/{order_id}/process-payment", partyId, orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId));

        verify(orderService, times(1)).processOrderPayment(partyId, orderId);
    }

    @Test
    void testDeleteOrder() throws Exception {
        Long partyId = 1L;
        Long guestId = 1L;
        Long orderId = 1L;
        doNothing().when(orderService).deleteOrder(partyId, guestId, orderId);


        mockMvc.perform(delete("/orders/{party_id}/{guest_id}/{order_id}", partyId, guestId, orderId))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).deleteOrder(partyId, guestId, orderId);
    }

    @Test
    void testDeleteOrder_Error() throws Exception {
        Long partyId = 1L;
        Long guestId = 1L;
        Long orderId = 1L;
        doThrow(new RuntimeException("Error deleting order")).when(orderService).deleteOrder(partyId, guestId, orderId);

        mockMvc.perform(delete("/orders/{party_id}/{guest_id}/{order_id}", partyId, guestId, orderId))
                .andExpect(status().isInternalServerError());

        verify(orderService, times(1)).deleteOrder(partyId, guestId, orderId);
    }
}

