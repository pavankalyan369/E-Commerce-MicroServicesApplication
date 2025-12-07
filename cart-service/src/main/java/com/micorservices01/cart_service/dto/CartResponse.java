package com.micorservices01.cart_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartResponse {

    private Long userId;
    private List<CartItemResponse> items;
    private Long totalAmount;
    private Long totalQuantity;
}

