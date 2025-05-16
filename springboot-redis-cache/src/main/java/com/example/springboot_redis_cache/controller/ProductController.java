package com.example.springboot_redis_cache.controller;

import com.example.springboot_redis_cache.entity.Product;
import com.example.springboot_redis_cache.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(path = "/save")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @GetMapping(path = "/get/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
         Product product =  productService.getProductById(id);
         return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @PutMapping(path = "/updateById/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable int id) {
        Product updatedProduct = productService.updateProduct(product, id);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }

    @DeleteMapping(path = "/deleteById/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        String message =  productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(message);
    }
}
