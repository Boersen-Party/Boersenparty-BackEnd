package com.boersenparty.v_1_1.controller;

import com.boersenparty.v_1_1.models.Order;
import com.boersenparty.v_1_1.models.Party;
import com.boersenparty.v_1_1.models.Product;
import com.boersenparty.v_1_1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1")
public class ProductController {
    @Autowired
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(path="/products")
    public List<Product> getProducts(){
        return productService.getProducts();
    }

    @PostMapping(path = "/product")
    public ResponseEntity<Product> createProduct(@RequestBody(required = false) Product product) {
        if (product == null){
            product = new Product();
        }

        return ResponseEntity.ok(productService.createProduct(product));
    }

}
