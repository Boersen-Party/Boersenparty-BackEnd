package com.boersenparty.v_1_1.interfaces;

import com.boersenparty.v_1_1.models.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping(path="/parties")
public interface OrderControllerInterface {
    @GetMapping(path="/{party_id}/guests/{guest_id}/orders")
    public List<Order>[] getOrders(@PathVariable Long party_id, @PathVariable Long guest_id);

    @DeleteMapping(path="/{party_id}/guests/{guest_id}/orders/{order_id}")
    public void deleteOrder(@PathVariable Long party_id,
                            @PathVariable Long guest_id,
                            @PathVariable Long order_id);

    @PostMapping(path="/{party_id}/guests/{guest_id}/orders")
    public ResponseEntity<Order> createOrder(
            @PathVariable Long party_id,
            @PathVariable Long guest_id,
            @RequestBody(required = true) Order order);

    @PutMapping(path="/{party_id}/guests/{guest_id}/orders/{order_id}")
    public ResponseEntity<Order> updateOrder(
            @RequestBody(required = true) Order order,
            @PathVariable Long party_id,
            @PathVariable Long guest_id,
            @PathVariable Long order_id);

}
