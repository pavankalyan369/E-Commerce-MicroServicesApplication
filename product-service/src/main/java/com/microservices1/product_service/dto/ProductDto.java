package com.microservices1.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private Long price;
    private String type;
    private Long weight;
    private Long stock;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
