package com.microservices01.user_service.service;

import com.microservices01.user_service.dto.UserDto;
import com.microservices01.user_service.dto.UserProfileDto;
import com.microservices01.user_service.dto.UserRegistrationDto;
import com.microservices01.user_service.entity.User;
import com.microservices01.user_service.entity.UserStatus;
import com.microservices01.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    // ------------ USER BASIC CRUD + SEARCH ------------ //

    public UserDto registerUser(UserRegistrationDto registrationDto) {
        if (userRepo.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .id(registrationDto.getId()) // if you use manual IDs
                .name(registrationDto.getName())
                .email(registrationDto.getEmail())
                .password(registrationDto.getPassword()) // hash in real app
                .adress(registrationDto.getAdress())
                .pin(registrationDto.getPin())
                .phone(registrationDto.getPhone())
                .status(UserStatus.ACTIVE)
                .build();

        User saved = userRepo.save(user);
        return mapUserToDto(saved);
    }

    public UserDto getUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return mapUserToDto(user);
    }

    public UserDto getUserByEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return mapUserToDto(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(this::mapUserToDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> searchUsers(String query) {
        return userRepo.findByNameContainingIgnoreCase(query)
                .stream()
                .map(this::mapUserToDto)
                .collect(Collectors.toList());
    }

    public UserDto updateBasicUser(Long id, UserRegistrationDto dto) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (dto.getName() != null) user.setName(dto.getName());

        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (userRepo.findByEmail(dto.getEmail()).isPresent()) {
                throw new RuntimeException("Email already in use");
            }
            user.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null) user.setPassword(dto.getPassword());
        if (dto.getAdress() != null) user.setAdress(dto.getAdress());
        if (dto.getPin() != null) user.setPin(dto.getPin());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());

        User saved = userRepo.save(user);
        return mapUserToDto(saved);
    }

    public void deleteUser(Long id) {
        if (!userRepo.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepo.deleteById(id);
    }

    public UserDto changeStatus(Long id, UserStatus status) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setStatus(status);
        User saved = userRepo.save(user);
        return mapUserToDto(saved);
    }

    // ------------ SPECIFIC PATCH HELPERS ------------ //

    public UserDto patchEmail(Long id, String email) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (userRepo.findByEmail(email).isPresent() && !user.getEmail().equals(email)) {
            throw new RuntimeException("Email already in use");
        }
        user.setEmail(email);

        return mapUserToDto(userRepo.save(user));
    }

    public UserDto patchPassword(Long id, String password) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setPassword(password); // hash in real app
        return mapUserToDto(userRepo.save(user));
    }

    public UserDto patchPhone(Long id, String phone) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setPhone(phone);
        return mapUserToDto(userRepo.save(user));
    }

    public UserDto patchAddress(Long id, String adress, Long pin) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (adress != null) user.setAdress(adress);
        if (pin != null) user.setPin(pin);

        return mapUserToDto(userRepo.save(user));
    }


    // ------------ PROFILE MANAGEMENT (same table) ------------ //

    public UserProfileDto getProfile(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return mapProfileToDto(user);
    }

    public UserProfileDto updateProfile(Long userId, UserProfileDto profileDto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (profileDto.getPhone() != null) user.setPhone(profileDto.getPhone());
        if (profileDto.getAdress() != null) user.setAdress(profileDto.getAdress());
        if (profileDto.getPin() != null) user.setPin(profileDto.getPin());
        if (profileDto.getDateOfBirth() != null) user.setDateOfBirth(profileDto.getDateOfBirth());
        if (profileDto.getGender() != null) user.setGender(profileDto.getGender());
       // if (profileDto.getAvatarUrl() != null) user.setAvatarUrl(profileDto.getAvatarUrl());

        User saved = userRepo.save(user);
        return mapProfileToDto(saved);
    }

    // ------------ MAPPERS ------------ //

    private UserDto mapUserToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAdress(user.getAdress());
        dto.setPin(user.getPin());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setGender(user.getGender());
      //  dto.setAvatarUrl(user.getAvatarUrl());
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    private UserProfileDto mapProfileToDto(User user) {
        UserProfileDto dto = new UserProfileDto();
        dto.setUserId(user.getId());
        dto.setPhone(user.getPhone());
        dto.setAdress(user.getAdress());
        dto.setPin(user.getPin());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setGender(user.getGender());
       // dto.setAvatarUrl(user.getAvatarUrl());
        return dto;
    }
}
