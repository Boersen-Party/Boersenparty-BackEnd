package com.boersenparty.v_1_1.service;

import com.boersenparty.v_1_1.dto.ProductDTO;
import com.boersenparty.v_1_1.mapper.ProductDTOMapper;
import com.boersenparty.v_1_1.models.Party;
import com.boersenparty.v_1_1.models.Product;
import com.boersenparty.v_1_1.repository.PartyRepository;
import com.boersenparty.v_1_1.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final PartyRepository partyRepository;

    public ProductService(ProductRepository productRepository, PartyRepository partyRepository) {

        this.productRepository = productRepository;
        this.partyRepository =  partyRepository;
    }

    public List<ProductDTO> getProducts(Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new IllegalArgumentException("Party not found with ID: " + partyId));

        return party.getProducts().stream()
                .map(ProductDTOMapper::toDTO)
                .toList();
    }

    public ProductDTO createProduct(ProductDTO productDTO, Long partyId) {
        System.out.println("Incoming ProductDTO: " + productDTO);

        // Find the party by ID
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new IllegalArgumentException("Party not found with ID: " + partyId));

        Product product = ProductDTOMapper.toEntity(productDTO);
        product.setParty(party);
        Product savedProduct = productRepository.save(product);

        return ProductDTOMapper.toDTO(savedProduct);
    }
}
