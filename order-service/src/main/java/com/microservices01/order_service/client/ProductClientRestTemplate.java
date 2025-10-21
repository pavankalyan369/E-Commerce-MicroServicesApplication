package com.microservices01.order_service.client;

import com.microservices01.order_service.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductClientRestTemplate {
    @Autowired
    private RestTemplate restTemplate;

    private static final String BASE_URL="http://localhost:8083/products/";

    public ProductDto getProductById(Long id) {
        String url = BASE_URL + id;
        return restTemplate.getForObject(url, ProductDto.class);
    }
}
