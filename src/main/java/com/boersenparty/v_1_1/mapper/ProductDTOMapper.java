package com.boersenparty.v_1_1.mapper;

import com.boersenparty.v_1_1.dto.ProductDTO;
import com.boersenparty.v_1_1.models.CalculatedPrice;
import com.boersenparty.v_1_1.models.Product;

import java.util.List;
import java.util.stream.Collectors;

import java.util.ArrayList;


public class ProductDTOMapper {

    // Convert Product entity to ProductDTO
    public static ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setpQuantity(product.getpQuantity());

        // Extract the price_base from the first CalculatedPrice in the list
        if (product.getCalculatedPrices() != null && !product.getCalculatedPrices().isEmpty()) {
            productDTO.setPrice_base(product.getCalculatedPrices().get(0).getPrice());
        }

        productDTO.setPrice_min(product.getPrice_min());
        productDTO.setPrice_max(product.getPrice_max());
        productDTO.setIs_active(product.isIs_active());
        productDTO.setImageURL(product.getImageURL());
        productDTO.setProductType(product.getProductType());

        return productDTO;
    }

    // Convert ProductDTO to Product entity
    public static Product toEntity(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setpQuantity(productDTO.getpQuantity());

        // Create a new CalculatedPrice list and add the first entry based on price_base
        if (productDTO.getPrice_base() != null) {
            List<CalculatedPrice> calculatedPrices = new ArrayList<>();
            CalculatedPrice calculatedPrice = new CalculatedPrice(product, productDTO.getPrice_base());
            calculatedPrices.add(calculatedPrice);
            product.setCalculatedPrices(calculatedPrices);
        }

        product.setPrice_min(productDTO.getPrice_min());
        product.setPrice_max(productDTO.getPrice_max());
        product.setIs_active((product.getpQuantity() > 0) ? true : false);
        product.setImageURL(productDTO.getImageURL());
        product.setProductType(productDTO.getProductType());

        return product;
    }

    public static List<ProductDTO> toDTOList(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return List.of();
        }
        return products.stream().map(ProductDTOMapper::toDTO).collect(Collectors.toList());
    }

    public static List<Product> toEntityList(List<ProductDTO> productDTOs) {
        if (productDTOs == null || productDTOs.isEmpty()) {
            return List.of();
        }
        return productDTOs.stream().map(ProductDTOMapper::toEntity).collect(Collectors.toList());
    }
}


