package com.microservices01.user_service.controller;

import com.microservices01.user_service.dto.UserDto;
import com.microservices01.user_service.entity.User;
import com.microservices01.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public void createUser(@RequestBody UserDto userDto){
        userService.createUser(userDto);
    }

    @GetMapping("/all")
    public List<User> getUsers(){
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }
}
