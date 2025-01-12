package com.boersenparty.v_1_1.controller;


import com.boersenparty.v_1_1.dto.PartyStatsDTO;
import com.boersenparty.v_1_1.models.Party;
import com.boersenparty.v_1_1.models.PartyStats;
import com.boersenparty.v_1_1.service.PartyService;
import com.boersenparty.v_1_1.service.PartyStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/parties")
@CrossOrigin
public class PartyStatsController {

    private final PartyStatsService partyStatsService;
    private final PartyService partyService;

    public PartyStatsController(PartyStatsService partyStatsService, PartyService partyService) {
        this.partyStatsService = partyStatsService;
        this.partyService = partyService;
    }

    @GetMapping("/{partyId}/partystats")
    public ResponseEntity<PartyStatsDTO> getPartyStats(@PathVariable Long partyId) {
        Optional<Party> party = partyService.findById(partyId);

        if (party.isEmpty()) {
            return ResponseEntity.ok(new PartyStatsDTO());
        }

        Optional<PartyStats> stats = partyStatsService.getStatsByParty(party.get());

        PartyStatsDTO partyStatsDTO = new PartyStatsDTO();
        if (stats.isPresent()) {
            PartyStats statsData = stats.get();
            partyStatsDTO.setPartyId(statsData.getParty().getId());
            partyStatsDTO.setRevenue(statsData.getRevenue());
            partyStatsDTO.setProfit(statsData.getProfit());
            partyStatsDTO.setTotalOrders(statsData.getTotalOrders());
        } else {
            partyStatsDTO.setPartyId(partyId);
            partyStatsDTO.setRevenue(0.0);
            partyStatsDTO.setProfit(0.0);
            partyStatsDTO.setTotalOrders(0);
        }

        return ResponseEntity.ok(partyStatsDTO);
    }




}

