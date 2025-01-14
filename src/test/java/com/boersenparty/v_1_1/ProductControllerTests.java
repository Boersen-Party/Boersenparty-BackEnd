package com.boersenparty.v_1_1;


import com.boersenparty.v_1_1.controller.ProductController;
import com.boersenparty.v_1_1.dto.ProductDTO;
import com.boersenparty.v_1_1.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ProductControllerTests {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void testGetProducts() throws Exception {
        Long partyId = 1L;
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Test Product");
        when(productService.getProducts(partyId)).thenReturn(Collections.singletonList(productDTO));

        mockMvc.perform(get("/products/{party_id}", partyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Product"));

        verify(productService, times(1)).getProducts(partyId);
    }

    @Test
    void testCreateProduct() throws Exception {
        Long partyId = 1L;
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("New Product");
        when(productService.createProduct(any(ProductDTO.class), eq(partyId)))
                .thenReturn(productDTO);

        mockMvc.perform(post("/products/{party_id}", partyId)
                        .contentType("application/json")
                        .content("{\"name\":\"New Product\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Product"));

        verify(productService, times(1)).createProduct(any(ProductDTO.class), eq(partyId));
    }

    @Test
    void testUpdateProduct() throws Exception {
        Long partyId = 1L;
        Long productId = 1L;
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Updated Product");
        when(productService.updateProduct(any(ProductDTO.class), eq(partyId), eq(productId)))
                .thenReturn(productDTO);

        mockMvc.perform(put("/products/{party_id}/{product_id}", partyId, productId)
                        .contentType("application/json")
                        .content("{\"name\":\"Updated Product\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"));

        verify(productService, times(1)).updateProduct(any(ProductDTO.class), eq(partyId), eq(productId));
    }

    @Test
    void testDeleteProduct() throws Exception {
        Long partyId = 1L;
        Long productId = 1L;
        doNothing().when(productService).deleteProduct(partyId, productId);

        mockMvc.perform(delete("/products/{party_id}/{product_id}", partyId, productId))
                .andExpect(status().isOk())
                .andExpect(content().string("Product with ID: " + productId + " deleted successfully"));

        verify(productService, times(1)).deleteProduct(partyId, productId);
    }

    @Test
    void testDeleteProduct_Error() throws Exception {
        Long partyId = 1L;
        Long productId = 1L;
        doThrow(new RuntimeException("Error deleting product")).when(productService).deleteProduct(partyId, productId);

        mockMvc.perform(delete("/products/{party_id}/{product_id}", partyId, productId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error deleting product with ID: " + productId + ". Error deleting product"));

        verify(productService, times(1)).deleteProduct(partyId, productId);
    }
}

