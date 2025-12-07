package com.microservices01.api_gateway.controller;

import com.microservices01.api_gateway.dto.AuthRequest;
import com.microservices01.api_gateway.dto.AuthResponse;
import com.microservices01.api_gateway.dto.RegisterRequest;
import com.microservices01.api_gateway.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    private final UserService users;

    public AuthController(UserService users) {
        this.users = users;
    }

    // Public: register normal USER (no role input)
    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest req) {
        users.registerUser(req);
    }

    // Admin-only: register ADMIN
    @PostMapping("/admin/register")
    public void registerAdmin(@RequestBody RegisterRequest req) {
        users.registerAdmin(req);
    }

    // Public: login
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest req) {
        return users.login(req.getUsername(), req.getPassword());
    }
}
