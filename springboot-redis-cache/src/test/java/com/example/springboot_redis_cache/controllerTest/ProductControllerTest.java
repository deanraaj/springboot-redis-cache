package com.example.springboot_redis_cache.controllerTest;

import com.example.springboot_redis_cache.controller.ProductController;
import com.example.springboot_redis_cache.entity.Product;
import com.example.springboot_redis_cache.repository.ProductRepo;
import com.example.springboot_redis_cache.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class) // ensures only web layer is loaded
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc; // is used to mock the service layer

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private ProductRepo productRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createProductTest() throws Exception {
        Product product = new Product(1, "Phone", 10000);

        Mockito.when(productService.save(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/product/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Phone"))
                .andExpect(jsonPath("$.price").value(10000));

    }

    @Test
    void getProductByIdTest() throws Exception {
        Product product = new Product(1, "Laptop", 25000);
        Mockito.when(productService.getProductById(1)).thenReturn(product);

        mockMvc.perform(get("/api/product/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(25000));
    }

    @Test
    void updateProductTest() throws Exception {
        Product product = new Product(1, "Laptop", 45000);
        Mockito.when(productService.updateProduct(any(Product.class), eq(1))).thenReturn(product);

        mockMvc.perform(put("/api/product/updateById/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(45000));
    }

    @Test
    void deleteProductTest() throws Exception {
        Product product = new Product(1, "TV", 24000);

        Mockito.when(productService.deleteProduct(eq(1))).thenReturn("Product is Deleted");

        mockMvc.perform(delete("/api/product/deleteById/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("Product is Deleted"));
    }


}
