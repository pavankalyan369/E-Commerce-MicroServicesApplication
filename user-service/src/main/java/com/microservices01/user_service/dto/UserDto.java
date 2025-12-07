package com.microservices01.user_service.dto;

import com.microservices01.user_service.entity.UserStatus;
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

    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
