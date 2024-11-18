package com.boersenparty.v_1_1.interfaces;

import com.boersenparty.v_1_1.models.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping(path="/parties")
public interface ProductControllerInterface {
    @GetMapping(path="/{party_id}/products")
    public List<Product> getProducts(@PathVariable Long party_id);

    @GetMapping(path="/{party_id}/products/{product_id}")
    public Optional<Product> getProduct(@PathVariable Long party_id, @PathVariable Long product_id);

    @DeleteMapping(path="/{party_id}/products/{product_id}")
    public void deleteProduct(@PathVariable Long party_id, @PathVariable Long product_id);

    @PostMapping(path="/{party_id}/products")
    public ResponseEntity<Product> createProduct(@RequestBody(required = true) Product product);

    @PutMapping(path="/{party_id}/products/{product_id}")
    public ResponseEntity<Product> updateProduct(
            @RequestBody(required = true) Product product,
            @PathVariable Long party_id,
            @PathVariable Long product_id);

}
