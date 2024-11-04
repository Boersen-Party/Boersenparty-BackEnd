package com.boersenparty.v_1_1.service;

import com.boersenparty.v_1_1.dto.OrderDTO;
import com.boersenparty.v_1_1.models.Order;
import com.boersenparty.v_1_1.models.PartyGuest;
import com.boersenparty.v_1_1.repository.OrderRepository;
import com.boersenparty.v_1_1.repository.PartyGuestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final PartyGuestRepository partyGuestRepository;

    public OrderService(OrderRepository orderRepository, PartyGuestRepository partyGuestRepository) {
        this.orderRepository = orderRepository;
        this.partyGuestRepository = partyGuestRepository;
    }

    public void addOrder(Order order) {
        // TODO: Make distinct id check on party_id, partyguest_id
        orderRepository.save(order);
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }


    // TODO: remake createOrder
    /*
    public Order createOrder(OrderDTO orderDTO) {
        PartyGuest partyGuest = partyGuestRepository.findById(orderDTO.getPartyguest_id())
                .orElseThrow(() -> new EntityNotFoundException("Invalid PartyGuest"));

        Order order = new Order(partyGuest, orderDTO.getParty_id());

        return orderRepository.save(order);
    }

     */
}
