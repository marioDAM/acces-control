package com.example.service;

import com.example.dto.CreateUserDTO;
import com.example.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(CreateUserDTO user);

    Optional<User> getUserById(Long id);

    Page<User> filterUsers(String name, Integer age, Pageable pageable);
}
