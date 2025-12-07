package com.microservices1.product_service.repository;

import com.microservices1.product_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // search by name
    List<Product> findByNameContainingIgnoreCase(String name);

    // filter by type
    List<Product> findByType(String type);
}
