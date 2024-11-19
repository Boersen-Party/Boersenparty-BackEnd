package com.boersenparty.v_1_1.repository;

import com.boersenparty.v_1_1.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository <Order, Long> {
    //Die Orders eines bestimmten Gastes, einer bestimmten Party
    List<Order> findByPartyIdAndPartyGuestId(Long partyId, Long partyGuestId);


}
