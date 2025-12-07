package com.microservices01.order_service.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserDto {

    private Long id;
    private String name;
    private String email;

    private String phone;
    private String adress;
    private Long pin;
    private LocalDate dateOfBirth;
    private String gender;
    private String avatarUrl;

    private String status;        // we don’t actually need to use it
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
