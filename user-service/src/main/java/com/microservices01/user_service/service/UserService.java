package com.microservices01.user_service.service;

import com.microservices01.user_service.dto.UserDto;
import com.microservices01.user_service.entity.User;
import com.microservices01.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    public void createUser(UserDto userDto){
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAdress(userDto.getAdress());
        user.setPin(userDto.getPin());
        userRepo.save(user);
    }

    public List<User> getAll(){
        return userRepo.findAll();
    }

    public UserDto getUserById(Long id){
        Optional<User> u =  userRepo.findById(id);
        User user = u.get();
        UserDto userDto= new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setAdress(user.getAdress());
        userDto.setPin(user.getPin());

        return userDto;

    }

}
