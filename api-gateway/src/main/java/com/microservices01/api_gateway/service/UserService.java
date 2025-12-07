package com.microservices01.api_gateway.service;

import com.microservices01.api_gateway.dto.AuthResponse;
import com.microservices01.api_gateway.dto.RegisterRequest;
import com.microservices01.api_gateway.entity.Role;
import com.microservices01.api_gateway.entity.User;
import com.microservices01.api_gateway.repository.RoleRepository;
import com.microservices01.api_gateway.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public UserService(UserRepository users,
                       RoleRepository roles,
                       PasswordEncoder encoder,
                       JwtService jwt) {
        this.users = users;
        this.roles = roles;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    private Role getOrCreateRole(String name) {
        return roles.findByName(name)
                .orElseGet(() -> roles.save(new Role(null, name)));
    }

    // Public: always creates a USER
    public void registerUser(RegisterRequest req) {
        if (users.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("username already exists");
        }
        Role userRole = getOrCreateRole("USER");

        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(encoder.encode(req.getPassword()));
        u.setEnabled(true);
        u.setRoles(Set.of(userRole));
        users.save(u);
    }

    // Admin-only: creates an ADMIN
    public void registerAdmin(RegisterRequest req) {
        if (users.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("username already exists");
        }
        Role adminRole = getOrCreateRole("ADMIN");

        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(encoder.encode(req.getPassword()));
        u.setEnabled(true);
        u.setRoles(Set.of(adminRole));
        users.save(u);
    }

    public AuthResponse login(String username, String rawPassword) {
        User user = users.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("bad credentials"));

        if (!user.isEnabled() || !encoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("bad credentials");
        }

        var rolesList = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        String token = jwt.generateToken(user.getId(), user.getUsername(), rolesList);
        return new AuthResponse(token, jwt.getExpiresIn(), user.getId());
    }
}
