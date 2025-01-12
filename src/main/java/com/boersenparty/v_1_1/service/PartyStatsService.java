package com.boersenparty.v_1_1.service;

import com.boersenparty.v_1_1.models.Party;
import com.boersenparty.v_1_1.models.PartyStats;
import com.boersenparty.v_1_1.repository.PartyStatsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PartyStatsService {

    private final PartyStatsRepository partyStatsRepository;

    public PartyStatsService(PartyStatsRepository partyStatsRepository) {
        this.partyStatsRepository = partyStatsRepository;
    }

    public Optional<PartyStats> getStatsByParty(Party party) {
        return partyStatsRepository.findByParty(party);
    }
}