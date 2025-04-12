package com.example.service;

import com.example.dto.CreateUserDTO;
import com.example.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(CreateUserDTO user);

    Optional<User> getUserById(Long id);

    List<User> filterUsers(String name, Integer age);
}
