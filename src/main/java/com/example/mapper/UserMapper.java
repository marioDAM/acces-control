package com.example.mapper;

import com.example.dto.CreateUserDTO;
import com.example.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {
    public User toEntity(CreateUserDTO dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setAge(dto.getAge());
        user.setCreatedAt(LocalDateTime.now());
        user.setPostalCode(dto.getPostalCode());
        user.setSubscriptionStatus(dto.getSubscriptionStatus());
        return user;
    }
}
