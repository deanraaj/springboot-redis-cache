package com.example.springboot_redis_cache;

import com.example.springboot_redis_cache.entity.Product;
import com.example.springboot_redis_cache.repository.ProductRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
class SpringbootRedisCacheApplicationTests {

	@Container
	@ServiceConnection
	static GenericContainer redis = new GenericContainer(DockerImageName.parse("redis:7.4.2"))
			.withExposedPorts(6379);

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private CacheManager cacheManager;

	@MockitoSpyBean
	private ProductRepo productRepSpy;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp(){
		productRepo.deleteAll();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void testCreateProductAndCacheIt() throws Exception {
		Product product = new Product();
		product.setName("Laptop");
		product.setPrice(4599.59);

		//step 1: create Product
		MvcResult mvcResult = mockMvc.perform(post("/api/product/save")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(product)))
				.andExpect(status().isCreated())
				.andReturn();

		Product createdProduct = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Product.class);

		// get product id
		int id = createdProduct.getId();

		//Step 2: checking if it is created or not
		Assertions.assertTrue(productRepo.findById(id).isPresent());

		//step 3: check cache is create
		Cache cache = cacheManager.getCache("PRODUCT_CACHE");

		Assertions.assertNotNull(cache);

		Assertions.assertNotNull(cache.get(id, Product.class));
	}

	@Test
	void testGetProductAndVerifyCache() throws Exception{

		// Step 1: Save product to Data base
		Product product = new Product();
		product.setName("Phone");
		product.setPrice(1000);
		Product savedProduct = productRepo.save(product);

		// Step 2: Fetch Product
		mockMvc.perform(MockMvcRequestBuilders.get("/api/product/get/"+savedProduct.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Phone"));

		Mockito.verify(productRepSpy, Mockito.times(1)).findById(savedProduct.getId());

		Mockito.clearInvocations(productRepSpy);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/product/get/"+savedProduct.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Phone"));

		Mockito.verify(productRepSpy, Mockito.times(0)).findById(savedProduct.getId());
	}

}
