package com.microservices01.order_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orderr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // user info
    private Long userId;
    private String userName;
    private String userEmail;
    private String userPhone;

    // shipping
    private String adress;        // keeping your original spelling
    private Long pin;

    // product info
    private Long productId;
    private String productName;
    private String productType;

    private Long quantity;
    private Long price;           // price per unit at time of order
    private Long totalAmount;     // quantity * price

    // order status, payment, tracking
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String paymentMode;   // e.g. CARD, COD, UPI
    private String trackingNumber;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        if (status == null) {
            status = OrderStatus.CREATED;
        }
        if (paymentStatus == null) {
            paymentStatus = PaymentStatus.PENDING;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
