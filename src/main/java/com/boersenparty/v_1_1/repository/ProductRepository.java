package com.boersenparty.v_1_1.repository;

import com.boersenparty.v_1_1.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByPartyId(Long id);
}
