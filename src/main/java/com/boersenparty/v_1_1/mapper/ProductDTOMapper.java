package com.boersenparty.v_1_1.mapper;

import com.boersenparty.v_1_1.dto.ProductDTO;
import com.boersenparty.v_1_1.models.Product;

import java.util.List;
import java.util.stream.Collectors;

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
        productDTO.setPrice_base(product.getPrice_base());
        productDTO.setPrice_min(product.getPrice_min());
        productDTO.setPrice_max(product.getPrice_max());
        productDTO.setIs_active(product.isIs_active());
        productDTO.setImageURL(product.getImageURL());
        productDTO.setProductType(product.getProductType());

        return productDTO;
    }

    public static Product toEntity(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setpQuantity(productDTO.getpQuantity());
        product.setPrice_base(productDTO.getPrice_base());
        product.setPrice_min(productDTO.getPrice_min());
        product.setPrice_max(productDTO.getPrice_max());
        product.setIs_active(productDTO.isIs_active());
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

