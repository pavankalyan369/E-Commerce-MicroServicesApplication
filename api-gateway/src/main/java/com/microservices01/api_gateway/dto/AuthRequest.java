package com.microservices01.api_gateway.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank private String username;
    @NotBlank private String password;
}
