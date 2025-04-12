package com.example.service.impl;

import com.example.dto.CreateUserDTO;
import com.example.mapper.UserMapper;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @Override
    public User createUser(CreateUserDTO dto) {
        User user = userMapper.toEntity(dto);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> filterUsers(String name, Integer age) {
        if (name == null && age == null) {
            throw new IllegalArgumentException("At least one filter parameter must be provided");
        }

        if (name != null && age != null) {
            return userRepository.findByFirstNameAndAge(name, age);
        } else if (name != null) {
            return userRepository.findByFirstName(name);
        } else {
            return userRepository.findByAge(age);
        }
    }
}
