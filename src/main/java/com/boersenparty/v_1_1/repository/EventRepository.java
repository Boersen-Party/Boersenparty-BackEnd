package com.boersenparty.v_1_1.repository;

import com.boersenparty.v_1_1.models.Event;
import com.boersenparty.v_1_1.models.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByParty(Party party);
}
