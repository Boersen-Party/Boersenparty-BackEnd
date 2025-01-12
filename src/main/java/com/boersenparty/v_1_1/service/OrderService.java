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



    public Order processOrderPayment(Long partyId, Long orderId) {
        System.out.println("Starting processOrderPayment for Party ID: " + partyId + ", Order ID: " + orderId);

        // Retrieve the order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    System.out.println("Order not found with ID: " + orderId);
                    return new RuntimeException("Order not found with ID: " + orderId);
                });
        System.out.println("Retrieved Order: " + order);

        // Validate that the order belongs to the specified party
        if (!order.getParty().getId().equals(partyId)) {
            System.out.println("Order does not belong to the specified party with ID: " + partyId);
            throw new RuntimeException("Order does not belong to the specified party with ID: " + partyId);
        }
        System.out.println("Order is valid for the specified party.");

        if (order.getIs_paid()) {  // Adjusted naming convention for 'isPaid'
            System.out.println("Order is already marked as paid.");
            throw new RuntimeException("Order is already marked as paid.");
        }

        // Mark the order as paid
        order.setIs_paid(true);
        System.out.println("Order marked as paid.");

        // Update PartyStats (assumes a PartyStats entity exists and is mapped to a party)
        Party party = order.getParty();
        System.out.println("Retrieving PartyStats for Party ID: " + party.getId());
        PartyStats partyStats = partyStatsRepository.findByParty(party)
                .orElseGet(() -> {
                    System.out.println("No existing PartyStats found. Creating new PartyStats for Party ID: " + party.getId());
                    PartyStats newPartyStats = new PartyStats();
                    newPartyStats.setParty(party);
                    return newPartyStats;
                });

        // Calculate revenue and profit
        double orderRevenue = order.getTotalPrice();
        double orderProfit = 0;
        System.out.println("Order revenue: " + orderRevenue);

        // Iterate through order items and calculate profits and adjust quantities
        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();
            System.out.println("Processing OrderItem: " + orderItem.getId() + ", Product ID: " + product.getId());

            // Calculate profit per item
            double itemProfit = (orderItem.getPricePerItem() - product.getPrice_min()) * orderItem.getQuantity();  // Adjusted naming convention
            orderProfit += itemProfit;
            System.out.println("Calculated item profit for Product ID: " + product.getId() + " is: " + itemProfit);

            // Reduce product quantity
            if (product.getpQuantity() < orderItem.getQuantity()) {  // Adjusted naming convention
                System.out.println("Insufficient product quantity for product ID: " + product.getId());
                throw new RuntimeException("Insufficient product quantity for product ID: " + product.getId());
            }
            product.setpQuantity(product.getpQuantity() - orderItem.getQuantity());  // Adjusted naming convention
            System.out.println("Reduced quantity for Product ID: " + product.getId() + " to: " + product.getpQuantity());
            productRepository.save(product);
        }

        // Update PartyStats
        partyStats.setRevenue(partyStats.getRevenue() + orderRevenue);
        partyStats.setProfit(partyStats.getProfit() + orderProfit);
        partyStats.setTotalOrders(partyStats.getTotalOrders() + 1);
        System.out.println("Updated PartyStats for Party ID: " + party.getId() + ". New revenue: " + partyStats.getRevenue() + ", New profit: " + partyStats.getProfit() + ", Total orders: " + partyStats.getTotalOrders());

        partyStatsRepository.save(partyStats);
        System.out.println("Saved updated PartyStats.");

        // Save the updated order
        Order updatedOrder = orderRepository.save(order);
        System.out.println("Saved updated Order with ID: " + updatedOrder.getId());

        return updatedOrder;
    }




}











