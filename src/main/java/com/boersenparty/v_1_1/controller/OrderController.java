package com.boersenparty.v_1_1.controller;

import com.boersenparty.v_1_1.dto.OrderDTO;
import com.boersenparty.v_1_1.interfaces.OrderControllerInterface;
import com.boersenparty.v_1_1.mapper.OrderDTOMapper;
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
    private final OrderDTOMapper orderMapper;


    public OrderController(OrderService orderService, OrderDTOMapper orderMapper) {

        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }



    @Override
    public List<OrderDTO> getOrders(Long party_id) {
        List<Order> orders = orderService.findOrdersByPartyId(party_id);
        return orderMapper.mapToOrderDTOList(orders);

    }

    @Override
    public List<OrderDTO> getUsersOrders( String uuid) {
        List<Order> orders = orderService.getOrdersByPartyGuestUuid(uuid); // Replace with actual service call
        return orderMapper.mapToOrderDTOList(orders);

    }


        @Override
    public void deleteOrder(Long party_id, Long guest_id, Long order_id){
        orderService.deleteOrder(party_id, guest_id, order_id);
    }

    @Override
    public ResponseEntity<OrderDTO> createReservation( Long party_id, OrderDTO orderDTO) {
        System.out.println("INCOMING ORDER DTO:" + orderDTO);
        try {
            Order createdOrder = orderService.createReservation(party_id, orderDTO);
            OrderDTO createdOrderDTO = orderMapper.mapToOrderDTO(createdOrder);
            return ResponseEntity.ok(createdOrderDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Override
    public ResponseEntity<Order> processOrderPayment(@PathVariable Long party_id, @PathVariable Long order_id) {
        Order paidOrder = orderService.processOrderPayment(party_id, order_id);
        return ResponseEntity.ok(paidOrder);
    }


}
