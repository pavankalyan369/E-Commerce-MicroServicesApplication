package com.microservices01.user_service.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileDto {

    private Long userId;

    private String phone;
    private String adress;
    private Long pin;

    private LocalDate dateOfBirth;
    private String gender;
    private String avatarUrl;
}
