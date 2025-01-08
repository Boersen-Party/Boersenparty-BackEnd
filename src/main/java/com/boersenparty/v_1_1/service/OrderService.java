package com.boersenparty.v_1_1.service;

import com.boersenparty.v_1_1.dto.OrderDTO;
import com.boersenparty.v_1_1.dto.OrderItemDTO;
import com.boersenparty.v_1_1.models.*;
import com.boersenparty.v_1_1.repository.OrderRepository;
import com.boersenparty.v_1_1.repository.PartyGuestRepository;
import com.boersenparty.v_1_1.repository.PartyRepository;
import com.boersenparty.v_1_1.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final PartyGuestRepository partyGuestRepository;
    private final PartyRepository partyRepository;
    private final ProductRepository productRepository;


    private final PartyGuestService partyGuestService;

    public OrderService(OrderRepository orderRepository, PartyGuestRepository partyGuestRepository,
                        PartyRepository partyRepository, PartyGuestService partyGuestService,
                         ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.partyGuestRepository = partyGuestRepository;
        this.partyRepository = partyRepository;
        this.partyGuestService = partyGuestService;
        this.productRepository = productRepository;


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

    //this doesn't use the partyId
    public Order createReservation(Long partyId, OrderDTO orderDTO) {
        // 1. Get PartyGuest from UUID
        //System.out.println("INCOMING ORDERDTO:" + orderDTO);
        Optional<PartyGuest> partyGuestOptional = partyGuestRepository.findByUuid(UUID.fromString(orderDTO.getUuid()));
        if (!partyGuestOptional.isPresent()) {
            throw new RuntimeException("PartyGuest not found for UUID: " + orderDTO.getUuid());
        }
        PartyGuest partyGuest = partyGuestOptional.get();
        //System.out.println("PartyGuest found: " + partyGuest);

        // 2. Create Order and set PartyGuest and Party
        Order order = new Order();
        order.setPartyGuest(partyGuest);
        order.setParty(partyGuest.getParty());  // Assuming PartyGuest has a reference to Party
        order.setIs_paid(orderDTO.isPaid());
        //System.out.println("Order created: " + order);

        // 3. Set OrderItems based on OrderDTO items
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDTO itemDTO : orderDTO.getItems()) {
            Optional<Product> productOptional = productRepository.findById(itemDTO.getProductId());
            if (productOptional.isPresent()) {
                Product product = productOptional.get();

                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setQuantity(itemDTO.getQuantity());
                orderItem.setPricePerItem(itemDTO.getPricePerItem());
                orderItem.setOrder(order);  // Set the order for this item

                orderItems.add(orderItem);
            }
        }
        System.out.println("OrderItems added: " + orderItems);

        // 4. Set the OrderItems and Save Order
        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        System.out.println("Order saved to DB: " + savedOrder);

        return savedOrder;
    }


}





 /*
    public Order realizeOrder(Long guestId) {

        PartyGuest partyGuest = partyGuestRepository.findById(guestId)
                .orElseThrow(() -> new EntityNotFoundException("Invalid PartyGuest"));

        Order order = new Order(partyGuest, partyGuest.getParty());
        return orderRepository.save(order);

         */






