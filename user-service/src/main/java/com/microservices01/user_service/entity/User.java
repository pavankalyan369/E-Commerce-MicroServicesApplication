package com.microservices01.user_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
//import lombok.Data;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    // keep as-is to match your existing table; add @GeneratedValue if you want auto IDs
    private Long id;

    private String name;
    private String email;

    // for real apps, hash this (BCrypt, etc.)
    private String password;

    // existing fields
    private String adress;
    private Long pin;

    // extra profile fields (still same table)
    private String phone;
    private LocalDate dateOfBirth;
    private String gender;
   // private String avatarUrl;   // profile picture URL

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        if (status == null) {
            status = UserStatus.ACTIVE;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
