package com.boersenparty.v_1_1.repository;

import com.boersenparty.v_1_1.models.Party;
import com.boersenparty.v_1_1.models.PartyStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartyStatsRepository extends JpaRepository<PartyStats, Long> {
    @Query("SELECT ps FROM PartyStats ps WHERE ps.party = :party")
    Optional<PartyStats> findByParty(@Param("party") Party party);}
