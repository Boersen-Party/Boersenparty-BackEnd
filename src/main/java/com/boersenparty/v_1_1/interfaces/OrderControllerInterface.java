package com.boersenparty.v_1_1.interfaces;

import com.boersenparty.v_1_1.dto.OrderDTO;
import com.boersenparty.v_1_1.models.Order;
import com.boersenparty.v_1_1.models.Party;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping(path="/parties")
public interface OrderControllerInterface {
    @GetMapping(path="/{party_id}/guests/orders") //for personaler
    public List<OrderDTO> getOrders(@PathVariable Long party_id);


    @GetMapping(path="/guests/orders")
    public List<OrderDTO> getUsersOrders(@RequestParam String uuid);



    @DeleteMapping(path="/{party_id}/guests/{guest_id}/orders/{order_id}")
    public void deleteOrder(@PathVariable Long party_id,
                            @PathVariable Long guest_id,
                            @PathVariable Long order_id);

    @PostMapping(path="/{party_id}/guests/orders")
    public ResponseEntity<OrderDTO> createReservation(@PathVariable Long party_id, @RequestBody OrderDTO reservationDTO);


    @PostMapping("/{party_id}/guests/orders/{order_id}")
    public ResponseEntity<Order> processOrderPayment(@PathVariable Long party_id, @PathVariable Long order_id);

}
