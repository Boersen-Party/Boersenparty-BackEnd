package com.boersenparty.v_1_1.service;

import com.boersenparty.v_1_1.dto.OrderDTO;
import com.boersenparty.v_1_1.dto.OrderItemDTO;
import com.boersenparty.v_1_1.models.*;
import com.boersenparty.v_1_1.repository.OrderRepository;
import com.boersenparty.v_1_1.repository.PartyGuestRepository;
import com.boersenparty.v_1_1.repository.PartyRepository;
import com.boersenparty.v_1_1.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public List<Order> getOrdersByPartyGuestUuid(String uuid) {
        return orderRepository.findByPartyGuestUuid(UUID.fromString(uuid));
    }

    public void deleteOrder(Long partyId, Long guestId, Long orderId) {
        // naked delete for now, check if exists before deleting?
        System.out.println("to be implemented");
    }

    //this doesn't use the partyId
    public Order createReservation(Long partyId, OrderDTO orderDTO) {
        // 1. Get PartyGuest from UUID
        Optional<PartyGuest> partyGuestOptional = partyGuestRepository.findByUuid(UUID.fromString(orderDTO.getBelongs_to()));
        if (!partyGuestOptional.isPresent()) {
            throw new RuntimeException("PartyGuest not found for UUID: " + orderDTO.getBelongs_to());
        }
        PartyGuest partyGuest = partyGuestOptional.get();

        // 2. Create Order and set PartyGuest and Party
        Order order = new Order();
        order.setPartyGuest(partyGuest);
        order.setParty(partyGuest.getParty());  // Assuming PartyGuest has a reference to Party
        order.setIs_paid(orderDTO.isPaid());
        order.setBelongsTo(orderDTO.getBelongs_to());
        order.setExpires_at(LocalDateTime.now().plusMinutes(6));


        // 3. Set OrderItems based on OrderDTO items and calculate total price
        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0;  // Initialize total price

        for (OrderItemDTO itemDTO : orderDTO.getItems()) {
            Optional<Product> productOptional = productRepository.findById(itemDTO.getProductId());
            if (productOptional.isPresent()) {
                Product product = productOptional.get();

                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setQuantity(itemDTO.getQuantity());
                orderItem.setPricePerItem(itemDTO.getPricePerItem());

                // Calculate total price for the item
                orderItem.setTotalItemPrice(itemDTO.getTotalItemPrice());

                orderItem.setOrder(order);  // Set the order for this item
                totalPrice += orderItem.getTotalItemPrice();  // Add to total order price

                orderItems.add(orderItem);
                System.out.println("added item is + " + orderItem);
            }
        }



        // 4. Set the OrderItems, Total Price, and Save Order
        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);  // Set the total price in the order
        Order savedOrder = orderRepository.save(order);
        System.out.println("the saved order is: "+ savedOrder);

        return savedOrder;
    }


    public List<Order> findOrdersByPartyId(Long partyId) {
        return orderRepository.findByPartyId(partyId);
    }


}





 /*
    public Order realizeOrder(Long guestId) {

        PartyGuest partyGuest = partyGuestRepository.findById(guestId)
                .orElseThrow(() -> new EntityNotFoundException("Invalid PartyGuest"));

        Order order = new Order(partyGuest, partyGuest.getParty());
        return orderRepository.save(order);

         */






