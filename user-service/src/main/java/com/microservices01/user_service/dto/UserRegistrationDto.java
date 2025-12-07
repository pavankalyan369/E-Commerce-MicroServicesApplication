package com.microservices01.user_service.dto;

import lombok.Data;

@Data
public class UserRegistrationDto {

    private Long id;          // optional, if you set IDs manually
    private String name;
    private String email;
    private String password;

    // basic profile fields on registration
    private String adress;
    private Long pin;
    private String phone;
}