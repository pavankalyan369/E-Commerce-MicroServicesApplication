package com.microservices01.order_service.dto;

import com.microservices01.order_service.entity.OrderStatus;
import com.microservices01.order_service.entity.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDto {

    private Long id;

    private Long userId;
    private String userName;
    private String userEmail;
    private String userPhone;

    private String adress;
    private Long pin;

    private Long productId;
    private String productName;
    private String productType;

    private Long quantity;
    private Long price;
    private Long totalAmount;

    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private String paymentMode;
    private String trackingNumber;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
