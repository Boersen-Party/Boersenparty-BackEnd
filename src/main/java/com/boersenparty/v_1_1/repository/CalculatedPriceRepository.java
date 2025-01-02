package com.boersenparty.v_1_1.repository;

import com.boersenparty.v_1_1.models.CalculatedPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalculatedPriceRepository extends JpaRepository<CalculatedPrice, Long> {
}

