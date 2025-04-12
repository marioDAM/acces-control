package com.example.service.impl;

import com.example.dto.CreateUserDTO;
import com.example.mapper.UserMapper;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @Override
    public User createUser(CreateUserDTO dto) {
        log.info("UserServiceImpl.createUser Inicio");
        try {
            User user = userMapper.toEntity(dto);
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> getUserById(Long id) {
        log.info("UserServiceImpl.getUserById Inicio, parameter: id {}", id);
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al consultar usuarios por su ID: " + e.getMessage(), e);
        }
    }

    public List<User> filterUsers(String name, Integer age) {
        log.info("UserServiceImpl.createUser Inicio parameter: name {} , age {}", name, age);
        try {
            if (name == null && age == null) {
                throw new IllegalArgumentException("Uno de los parámetros del filtro se tiene que rellenar");
            }

            if (name != null && age != null) {
                return userRepository.findByFirstNameAndAge(name, age);
            } else if (name != null) {
                return userRepository.findByFirstName(name);
            } else {
                return userRepository.findByAge(age);
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al consultar usuarios: " + e.getMessage(), e);
        }
    }
}
