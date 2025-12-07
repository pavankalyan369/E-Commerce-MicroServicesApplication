package com.micorservices01.cart_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
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

