package com.microservices01.user_service.repository;

import com.microservices01.user_service.entity.User;
import com.microservices01.user_service.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    List<User> findByNameContainingIgnoreCase(String name);

    List<User> findByStatus(UserStatus status);
}
