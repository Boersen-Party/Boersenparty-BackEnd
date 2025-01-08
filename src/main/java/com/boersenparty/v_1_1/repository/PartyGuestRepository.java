package com.boersenparty.v_1_1.repository;
import com.boersenparty.v_1_1.models.PartyGuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PartyGuestRepository extends JpaRepository <PartyGuest, Long> {
    Optional<PartyGuest> findByUuid(UUID uuid);



}
