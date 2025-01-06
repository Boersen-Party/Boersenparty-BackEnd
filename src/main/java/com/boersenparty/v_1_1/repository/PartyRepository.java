package com.boersenparty.v_1_1.repository;

import com.boersenparty.v_1_1.models.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {
    List<Party> findByHostedBy(String hostedBy);
    Optional<Party> findByAccessCode(String accessCode);

}
