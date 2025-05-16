package com.example.springboot_redis_cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringbootRedisCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootRedisCacheApplication.class, args);
	}

}
