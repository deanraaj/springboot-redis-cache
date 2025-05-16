# Spring Boot Redis Cache

This is a simple Spring Boot project demonstrating CRUD operations with Redis caching using Spring Cache.

## 🚀 Features

- Add, update, get, and delete products
- Redis cache using `@Cacheable`, `@CachePut`, and `@CacheEvict`
- Spring Data JPA integration
- JUnit 5 and Mockito-based unit testing
- RESTful API with Spring Boot

## 🛠️ Tech Stack

- Java 17+
- Spring Boot
- Spring Data JPA
- Redis
- MySQL or H2
- JUnit & Mockito
- Maven

## 🔗 API Endpoints

| Method | Endpoint        | Description           |
|--------|------------------|-----------------------|
| POST   | `/product`       | Create a new product  |
| GET    | `/product/{id}`  | Get product by ID     |
| PUT    | `/product/{id}`  | Update product by ID  |
| DELETE | `/product/{id}`  | Delete product by ID  |
