package com.micorservices01.cart_service.dto;

import lombok.Data;

@Data
public class CartItemResponse {

    private Long productId;
    private String productName;
    private String productType;
    private Long price;
    private Long quantity;
    private Long lineTotal;
}

