package com.boersenparty.v_1_1.controller;

import com.boersenparty.v_1_1.events.PriceUpdateEvent;
import com.boersenparty.v_1_1.models.CalculatedPrice;
import com.boersenparty.v_1_1.models.Product;
import com.boersenparty.v_1_1.repository.CalculatedPriceRepository;
import com.boersenparty.v_1_1.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/price-update")
public class PriceUpdateController {

    private final CalculatedPriceRepository calculatedPriceRepository;
    private final ProductRepository productRepository;

    public PriceUpdateController(CalculatedPriceRepository calculatedPriceRepository, ProductRepository productRepository) {
        this.calculatedPriceRepository = calculatedPriceRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> updatePrice(@PathVariable Long id, @RequestBody PriceUpdateEvent priceUpdateEvent) {
        // Fetch the product by ID
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        Product product = optionalProduct.get();

        // Create a new CalculatedPrice instance
        CalculatedPrice calculatedPrice = new CalculatedPrice();
        calculatedPrice.setProduct(product);
        calculatedPrice.setPrice(priceUpdateEvent.getCalculatedPrice().getPrice());
        calculatedPrice.setTime(priceUpdateEvent.getCalculatedPrice().getTime());

        // Save the CalculatedPrice
        System.out.println("CP DB looks like:" +        calculatedPriceRepository.save(calculatedPrice));


        // Associate the new price with the product
        List<CalculatedPrice> calculatedPrices = product.getCalculatedPrices();
        calculatedPrices.add(calculatedPrice);
        product.setCalculatedPrices(calculatedPrices);
        System.out.println("Product DB looks like:" +    productRepository.save(product));  // Update the product with the new price list

        System.out.println("PUC Price update received for product ID: " + id);
        return ResponseEntity.ok("Price update successful");
    }
}


