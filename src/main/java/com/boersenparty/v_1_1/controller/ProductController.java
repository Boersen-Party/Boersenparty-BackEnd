package com.boersenparty.v_1_1.controller;

import com.boersenparty.v_1_1.interfaces.ProductControllerInterface;
import com.boersenparty.v_1_1.models.Order;
import com.boersenparty.v_1_1.models.Party;
import com.boersenparty.v_1_1.models.Product;
import com.boersenparty.v_1_1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public List<Product> getProducts(Long party_id) {
        throw new UnsupportedOperationException("Method not implemented yet");
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
    public ResponseEntity<Product> createProduct(Product product){
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public ResponseEntity<Product> updateProduct(
             Product product,
             Long party_id,
             Long product_id){
        throw new UnsupportedOperationException("Method not implemented yet");
    }



}
