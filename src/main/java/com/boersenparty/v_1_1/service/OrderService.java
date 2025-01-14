package com.boersenparty.v_1_1.service;

import com.boersenparty.v_1_1.dto.OrderDTO;
import com.boersenparty.v_1_1.dto.OrderItemDTO;
import com.boersenparty.v_1_1.models.*;
import com.boersenparty.v_1_1.repository.*;
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
    private final PartyStatsRepository partyStatsRepository;

    public OrderService(OrderRepository orderRepository, PartyGuestRepository partyGuestRepository,
                        PartyRepository partyRepository, PartyGuestService partyGuestService,
                         ProductRepository productRepository, PartyStatsRepository partyStatsRepository) {
        this.orderRepository = orderRepository;
        this.partyGuestRepository = partyGuestRepository;
        this.partyRepository = partyRepository;
        this.partyGuestService = partyGuestService;
        this.productRepository = productRepository;
        this.partyStatsRepository = partyStatsRepository;


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
        System.out.println("Looking up PartyGuest with UUID: " + orderDTO.getBelongs_to());
        Optional<PartyGuest> partyGuestOptional = partyGuestRepository.findByUuid(UUID.fromString(orderDTO.getBelongs_to()));
        if (!partyGuestOptional.isPresent()) {
            System.out.println("PartyGuest not found for UUID: " + orderDTO.getBelongs_to());
            throw new RuntimeException("PartyGuest not found for UUID: " + orderDTO.getBelongs_to());
        }
        PartyGuest partyGuest = partyGuestOptional.get();
        System.out.println("Found PartyGuest: " + partyGuest);

        System.out.println("Checking for existing pending order for PartyGuest with UUID: " + orderDTO.getBelongs_to());
        Order existingOrder = orderRepository.findFirstByPartyGuestAndIsPaidFalseAndExpiresAtBefore(partyGuest, LocalDateTime.now());

        if (existingOrder != null) {
            System.out.println("Found existing pending order: " + existingOrder);
            System.out.println("Deleting pending order: " + existingOrder);
            orderRepository.delete(existingOrder);
        } else {
            System.out.println("No existing pending order found for PartyGuest with UUID: " + orderDTO.getBelongs_to());
        }

        Order order = new Order();
        order.setPartyGuest(partyGuest);
        order.setParty(partyGuest.getParty());
        order.setIs_paid(orderDTO.isPaid());
        order.setBelongsTo(orderDTO.getBelongs_to());
        order.setExpires_at(LocalDateTime.now().plusMinutes(6));  // in 6 min verfällt Reservation

        System.out.println("Creating new order for PartyGuest: " + partyGuest);
        System.out.println("Order details: " + order);

        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0;

        for (OrderItemDTO itemDTO : orderDTO.getItems()) {
            System.out.println("Processing OrderItem: " + itemDTO);
            Optional<Product> productOptional = productRepository.findById(itemDTO.getProductId());
            if (productOptional.isPresent()) {
                Product product = productOptional.get();

                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setQuantity(itemDTO.getQuantity());
                orderItem.setPricePerItem(itemDTO.getPricePerItem());

                orderItem.setTotalItemPrice(itemDTO.getTotalItemPrice());

                orderItem.setOrder(order);
                totalPrice += orderItem.getTotalItemPrice();

                orderItems.add(orderItem);
                System.out.println("Added item to order: " + orderItem);
            } else {
                System.out.println("Product not found for Product ID: " + itemDTO.getProductId());
            }
        }
        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);
        System.out.println("Final total price for order: " + totalPrice);
        Order savedOrder = orderRepository.save(order);
        System.out.println("Saved order: " + savedOrder);

        return savedOrder;
    }



    public List<Order> findOrdersByPartyId(Long partyId) {
        return orderRepository.findByPartyId(partyId);
    }



    public Order processOrderPayment(Long partyId, Long orderId) {
        System.out.println("Starting processOrderPayment for Party ID: " + partyId + ", Order ID: " + orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    System.out.println("Order not found with ID: " + orderId);
                    return new RuntimeException("Order not found with ID: " + orderId);
                });
        System.out.println("Retrieved Order: " + order);

        if (!order.getParty().getId().equals(partyId)) {
            throw new RuntimeException("Order does not belong to the specified party with ID: " + partyId);
        }

        if (order.getIs_paid()) {
            throw new RuntimeException("Order is already marked as paid.");
        }

        order.setIs_paid(true);

        Party party = order.getParty();
        System.out.println("Retrieving PartyStats for Party ID: " + party.getId());
        PartyStats partyStats = partyStatsRepository.findByParty(party)
                .orElseGet(() -> {
                    System.out.println("No existing PartyStats found. Creating new PartyStats for Party ID: " + party.getId());
                    PartyStats newPartyStats = new PartyStats();
                    newPartyStats.setParty(party);
                    return newPartyStats;
                });

        double orderRevenue = order.getTotalPrice();
        double orderProfit = 0;
        System.out.println("Order revenue: " + orderRevenue);

        //Gewinn ermitteln
        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();
            System.out.println("Processing OrderItem: " + orderItem.getId() + ", Product ID: " + product.getId());

            // Gewinn per item
            double itemProfit = (orderItem.getPricePerItem() - product.getPrice_min()) * orderItem.getQuantity();  // Adjusted naming convention
            orderProfit += itemProfit;
            System.out.println("Calculated item profit for Product ID: " + product.getId() + " is: " + itemProfit);

            // Bestand check
            if (product.getpQuantity() < orderItem.getQuantity()) {  // Adjusted naming convention
                throw new RuntimeException("Insufficient product quantity for product ID: " + product.getId());
            }
            product.setpQuantity(product.getpQuantity() - orderItem.getQuantity());  // Adjusted naming convention
            productRepository.save(product);
        }

        // Update PartyStats
        partyStats.setRevenue(partyStats.getRevenue() + orderRevenue);
        partyStats.setProfit(partyStats.getProfit() + orderProfit);
        partyStats.setTotalOrders(partyStats.getTotalOrders() + 1);

        partyStatsRepository.save(partyStats);

        Order updatedOrder = orderRepository.save(order);

        return updatedOrder;
    }




}











