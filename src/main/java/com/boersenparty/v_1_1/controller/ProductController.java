package com.boersenparty.v_1_1.controller;

import com.boersenparty.v_1_1.dto.ProductDTO;
import com.boersenparty.v_1_1.interfaces.ProductControllerInterface;
import com.boersenparty.v_1_1.models.Product;
import com.boersenparty.v_1_1.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public Optional<Product> getProduct(Long party_id, Long product_id) {
        return Optional.empty();
    }
    @Override
    public List<ProductDTO> getProducts(Long party_id) {
        return productService.getProducts(party_id);
    }




    /*
    @Override
    @PreAuthorize("hasAuthority('_VERANSTALTER')")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<String> deleteProduct(Long party_id,  Long product_id) {
        productService.deleteProduct(party_id, product_id);
        return ResponseEntity.ok("Product with ID:" + product_id + "deleted successfully");
    }
    */


    @Override
    @PreAuthorize("hasAuthority('_VERANSTALTER')")
    public ResponseEntity<String> deleteProduct(Long party_id,  Long product_id) {
        // Log the entire request object for detailed debugging

        System.out.println("AM I EVEN ENTERING deletePRODUCTS????");
        System.out.println("AM I EVEN ENTERING deletePRODUCTS????");
        System.out.println("AM I EVEN ENTERING deletePRODUCTS????");
        System.out.println("AM I EVEN ENTERING deletePRODUCTS????");
        System.out.println("AM I EVEN ENTERING deletePRODUCTS????");
        System.out.println("AM I EVEN ENTERING deletePRODUCTS????");
        System.out.println("AM I EVEN ENTERING deletePRODUCTS????");
        System.out.println("AM I EVEN ENTERING deletePRODUCTS????");
        System.out.println("AM I EVEN ENTERING deletePRODUCTS????");
        System.out.println("AM I EVEN ENTERING deletePRODUCTS????");


        try {
            // Call the service to delete the product
            productService.deleteProduct(party_id, product_id);

            return ResponseEntity.ok("Product with ID: " + product_id + " deleted successfully");
        } catch (Exception e) {
            // Log error and give detailed message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting product with ID: " + product_id + ". " + e.getMessage());
        }
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
