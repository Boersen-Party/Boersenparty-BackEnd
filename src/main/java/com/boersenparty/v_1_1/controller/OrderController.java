package com.boersenparty.v_1_1.controller;

import com.boersenparty.v_1_1.dto.OrderDTO;
import com.boersenparty.v_1_1.interfaces.OrderControllerInterface;
import com.boersenparty.v_1_1.models.Order;
import com.boersenparty.v_1_1.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
public class OrderController implements OrderControllerInterface {

    @Autowired
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public List<Order> getOrders(Long party_id,  Long guest_id){

        return orderService.getOrders(party_id, guest_id);
    }


    @Override
    public void deleteOrder(Long party_id, Long guest_id, Long order_id){
        orderService.deleteOrder(party_id, guest_id, order_id);
    }

    @Override
    public ResponseEntity<Order> createOrder(Long party_id, Long guest_id, Order order){
        return orderService.createOrder(party_id, guest_id, order);
    }

    @Override
    public ResponseEntity<Order> updateOrder(Order order, Long party_id,
                                             Long guest_id, Long order_id){
        System.out.println("updateOrder not yet implemented");
        return null;
    }


}
