package com.microservices01.order_service.dto;

import lombok.Data;

@Data
public class OrderRequestDto {

    private Long userId;
    private Long productId;
    private Long quantity;

    private String paymentMode;   // optional
}
