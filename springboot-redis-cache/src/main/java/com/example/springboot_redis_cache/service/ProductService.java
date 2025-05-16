package com.example.springboot_redis_cache.service;


import com.example.springboot_redis_cache.entity.Product;
import com.example.springboot_redis_cache.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

//    private final ProductRepo productRepo;
//
//    public ProductService(ProductRepo productRepo) {
//        this.productRepo = productRepo;
//    }

    @Autowired
    private ProductRepo productRepo;

    //@Autowired
    //private CacheManager cacheManager;

    @CachePut(value = "PRODUCT_CACHE", key = "#result.id")
    public Product save(Product product) {
        Product savedProduct = productRepo.save(product);

//        Cache productCache = cacheManager.getCache("PRODUCT_CACHE");
//        productCache.put(savedProduct.getId(), savedProduct);


        return savedProduct;
    }

    @Cacheable(value = "PRODUCT_CACHE", key = "#id")
    public Product getProductById(int id) {
        Product product = productRepo.findById(id).orElseThrow(() -> new RuntimeException("Product not found with id " + id));
        return product;
    }

    @CachePut(value = "PRODUCT_CACHE", key = "#result.id()")
    public Product updateProduct(Product product, int id) {
        Product pro = productRepo.findById(id).orElse(null);

        assert pro != null;
        pro.setName(product.getName());
        pro.setPrice(product.getPrice());
        Product savedProduct = productRepo.save(pro);
        return savedProduct;
    }

    @CacheEvict(value = "PRODUCT_CACHE", key = "#id")
    public String deleteProduct(int id) {
        productRepo.deleteById(id);
        return "Product is Deleted";
    }
}
