package com.boersenparty.v_1_1.service;

import com.boersenparty.v_1_1.models.Order;
import com.boersenparty.v_1_1.models.Party;
import com.boersenparty.v_1_1.models.PartyGuest;
import com.boersenparty.v_1_1.repository.OrderRepository;
import com.boersenparty.v_1_1.repository.PartyGuestRepository;
import com.boersenparty.v_1_1.repository.PartyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final PartyGuestRepository partyGuestRepository;
    private final PartyRepository partyRepository;

    private final PartyGuestService partyGuestService;

    public OrderService(OrderRepository orderRepository, PartyGuestRepository partyGuestRepository,
                        PartyRepository partyRepository, PartyGuestService partyGuestService) {
        this.orderRepository = orderRepository;
        this.partyGuestRepository = partyGuestRepository;
        this.partyRepository = partyRepository;
        this.partyGuestService = partyGuestService;
    }


    // ALL Orders of a particular guest, in a particular party
    public List<Order> getOrders(Long party_id, Long guest_id) {
        partyRepository.findById(party_id)
                .orElseThrow(() -> new RuntimeException("Party with ID " + party_id + " not found"));

        partyGuestRepository.findById(guest_id)
                .orElseThrow(() -> new RuntimeException("PartyGuest with ID " + guest_id + " not found"));


        //Prob have to check out the naming conventions of jparepository
        return orderRepository.findByPartyIdAndPartyGuestId(party_id, guest_id);
    }

    public void deleteOrder(Long partyId, Long guestId, Long orderId) {
        // naked delete for now, check if exists before deleting?
        System.out.println("to be implemented");
    }

    public ResponseEntity<Order> createOrder(Long party_id, Long guest_id, Order order) {
        /*
        System.out.println(order);
        // 1. find guest inside of said party
        Optional <PartyGuest> optionalguest = partyGuestService.getPartyGuestInParty(party_id, guest_id);
        PartyGuest partyguest = optionalguest.get();


        // Order NEEDS a mapping to PartyGuest, Party to instantiate
        order.setPartyGuest(partyguest);
        order.setParty(partyguest.getParty());

        // 2. Add Order to said guest
        partyguest.addOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);

         */

        return null;
    }






    public Order realizeOrder(Long guestId) {
        PartyGuest partyGuest = partyGuestRepository.findById(guestId)
                .orElseThrow(() -> new EntityNotFoundException("Invalid PartyGuest"));

        Order order = new Order(partyGuest, partyGuest.getParty());
        return orderRepository.save(order);
    }




}
