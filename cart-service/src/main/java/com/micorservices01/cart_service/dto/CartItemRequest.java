package com.micorservices01.cart_service.dto;


import lombok.Data;

@Data
public class CartItemRequest {
    private Long productId;
    private Long quantity;
}

