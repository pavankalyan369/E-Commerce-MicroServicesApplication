package com.microservices01.user_service.controller;

import com.microservices01.user_service.dto.UserDto;
import com.microservices01.user_service.dto.UserProfileDto;
import com.microservices01.user_service.dto.UserRegistrationDto;
import com.microservices01.user_service.entity.UserStatus;
import com.microservices01.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // --------- USER BASIC --------- //

    @PostMapping("/register")
    public UserDto register(@RequestBody UserRegistrationDto dto) {
        return userService.registerUser(dto);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/by-email")
    public UserDto getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/all")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/search")
    public List<UserDto> search(@RequestParam String q) {
        return userService.searchUsers(q);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserRegistrationDto dto) {
        return userService.updateBasicUser(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    // change status – already PATCH
    @PatchMapping("/{id}/status")
    public UserDto changeStatus(@PathVariable Long id, @RequestParam UserStatus status) {
        return userService.changeStatus(id, status);
    }

    // --------- SPECIFIC PATCH ENDPOINTS --------- //

    // PATCH email only
    @PatchMapping("/{id}/email")
    public UserDto patchEmail(@PathVariable Long id, @RequestParam String email) {
        return userService.patchEmail(id, email);
    }

    // PATCH password only
    @PatchMapping("/{id}/password")
    public UserDto patchPassword(@PathVariable Long id, @RequestParam String password) {
        return userService.patchPassword(id, password);
    }

    // PATCH phone only
    @PatchMapping("/{id}/phone")
    public UserDto patchPhone(@PathVariable Long id, @RequestParam String phone) {
        return userService.patchPhone(id, phone);
    }

    // PATCH address / pin (you can send one or both)
    @PatchMapping("/{id}/address")
    public UserDto patchAddress(
            @PathVariable Long id,
            @RequestParam(required = false) String adress,
            @RequestParam(required = false) Long pin
    ) {
        return userService.patchAddress(id, adress, pin);
    }


    // --------- PROFILE MANAGEMENT (same table) --------- //

    @GetMapping("/{id}/profile")
    public UserProfileDto getProfile(@PathVariable Long id) {
        return userService.getProfile(id);
    }

    @PutMapping("/{id}/profile")
    public UserProfileDto updateProfile(@PathVariable Long id, @RequestBody UserProfileDto profileDto) {
        profileDto.setUserId(id);
        return userService.updateProfile(id, profileDto);
    }
}
