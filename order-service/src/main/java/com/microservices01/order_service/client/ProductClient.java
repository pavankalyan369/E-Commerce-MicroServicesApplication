package com.microservices01.order_service.client;


import com.microservices01.order_service.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="product-service")
public interface ProductClient {
        @GetMapping("/products/{id}")
        ProductDto getProductById(@PathVariable("id") Long id);
}
