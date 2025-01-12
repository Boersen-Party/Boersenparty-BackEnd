package com.boersenparty.v_1_1.repository;

import com.boersenparty.v_1_1.models.Party;
import com.boersenparty.v_1_1.models.PartyStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartyStatsRepository extends JpaRepository<PartyStats, Long> {
    Optional<PartyStats> findByParty(Party party);
}
