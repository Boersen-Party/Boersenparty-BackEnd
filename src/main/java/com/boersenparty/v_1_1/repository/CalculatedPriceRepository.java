package com.boersenparty.v_1_1.repository;

import com.boersenparty.v_1_1.models.CalculatedPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalculatedPriceRepository extends JpaRepository<CalculatedPrice, Long> {
    @Query("SELECT c FROM CalculatedPrice c WHERE c.product.id = :productId ORDER BY c.time DESC")
    List<CalculatedPrice> findByProductId(@Param("productId") Long productId);

}

