package com.boersenparty.v_1_1.service;

import com.boersenparty.v_1_1.models.Product;
import com.boersenparty.v_1_1.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();

    }

    public Product createProduct(Product product) {
        // TODO: check product here
        return productRepository.save(product);
    }
}
