package com.boersenparty.v_1_1.controller;

import com.boersenparty.v_1_1.dto.ProductDTO;
import com.boersenparty.v_1_1.interfaces.ProductControllerInterface;
import com.boersenparty.v_1_1.models.Product;
import com.boersenparty.v_1_1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController implements ProductControllerInterface {
    @Autowired
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @Override
    public List<ProductDTO> getProducts(Long party_id) {
        return productService.getProducts(party_id);
    }


    @Override
    public Optional<Product> getProduct(Long party_id, Long product_id){
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public void deleteProduct(Long party_id,  Long product_id){
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    @PreAuthorize("hasAuthority('_VERANSTALTER')")
    public ResponseEntity<ProductDTO> createProduct(ProductDTO productDTO, Long party_id){
        return ResponseEntity.ok(productService.createProduct(productDTO, party_id));
    }

    @Override
    public ResponseEntity<Product> updateProduct(
             Product product,
             Long party_id,
             Long product_id){
        throw new UnsupportedOperationException("Method not implemented yet");
    }



}
