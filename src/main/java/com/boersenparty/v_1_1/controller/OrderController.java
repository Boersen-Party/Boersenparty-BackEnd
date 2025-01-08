package com.boersenparty.v_1_1.controller;

import com.boersenparty.v_1_1.dto.OrderDTO;
import com.boersenparty.v_1_1.interfaces.OrderControllerInterface;
import com.boersenparty.v_1_1.models.Order;
import com.boersenparty.v_1_1.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
public class OrderController implements OrderControllerInterface {

    @Autowired
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }




    @Override
    public List<Order>[] getOrders(Long party_id, Long guest_id){
        return null;
    }



    @Override
    public void deleteOrder(Long party_id, Long guest_id, Long order_id){
        orderService.deleteOrder(party_id, guest_id, order_id);
    }

    @Override
    public ResponseEntity<Order> createReservation( Long party_id, OrderDTO orderDTO) {
        System.out.println("INCOMING ORDER DTO:" + orderDTO);
        try {
            Order createdOrder = orderService.createReservation(party_id, orderDTO);
            return ResponseEntity.ok(createdOrder);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Order> updateOrder(Order order, Long party_id,
                                             Long guest_id, Long order_id){
        System.out.println("updateOrder not yet implemented");
        return null;
    }


}
