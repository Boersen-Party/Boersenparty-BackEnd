package com.boersenparty.v_1_1.interfaces;

import com.boersenparty.v_1_1.dto.ProductDTO;
import com.boersenparty.v_1_1.models.Product;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping(path="/parties")
public interface ProductControllerInterface {
    @GetMapping(path="/{party_id}/products")
    public List<ProductDTO> getProducts(@PathVariable Long party_id);

    @GetMapping(path="/{party_id}/products/{product_id}")
    public Optional<Product> getProduct(@PathVariable Long party_id, @PathVariable Long product_id);

    @DeleteMapping(path="/{party_id}/products/{product_id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long party_id, @PathVariable Long product_id);

    @PostMapping(path="/{party_id}/products")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody(required = true)
                                                 ProductDTO productDTO, @PathVariable Long party_id);

    @PutMapping(path="/{party_id}/products/{product_id}")
    public ResponseEntity<Product> updateProduct(
            @RequestBody(required = true) Product product,
            @PathVariable Long party_id,
            @PathVariable Long product_id);

}
