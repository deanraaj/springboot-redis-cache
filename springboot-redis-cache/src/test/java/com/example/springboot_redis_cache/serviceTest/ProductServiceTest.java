package com.example.springboot_redis_cache.serviceTest;

import com.example.springboot_redis_cache.entity.Product;
import com.example.springboot_redis_cache.repository.ProductRepo;
import com.example.springboot_redis_cache.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private ProductService productService;

    private Product sample;

    @BeforeEach
    void setUp() {
        sample = new Product(1, "Laptop", 50000);
    }

    @Test
    void saveProductTest() {
//        when(productRepo.save(sample)).thenReturn(sample);
        Mockito.when(productRepo.save(sample)).thenReturn(sample);
        Product savedProduct = productRepo.save(sample);

        Assertions.assertSame(sample, savedProduct);
        Assertions.assertEquals(sample.getId(), savedProduct.getId());
    }

    @Test
    void getProductByIdTest_shouldReturnProduct() {
        Mockito.when(productRepo.findById(1)).thenReturn(Optional.of(sample));

        Product result = productService.getProductById(1);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Laptop", result.getName());
        Assertions.assertEquals(50000, result.getPrice());
    }

    @Test
    void getProductByIdTest_productNotFound() {
        Mockito.when(productRepo.findById(10)).thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> productService.getProductById(10));
        Assertions.assertEquals(exception.getMessage(), "Product not found with id 10");
    }

    // Test case for deleting the product by id
    @Test
    void deleteProductByIdTest() {
        Mockito.doNothing().when(productRepo).deleteById(1);
        String result = productService.deleteProduct(1);
        Assertions.assertEquals(result, "Product is Deleted");
    }

}
