package com.example.service.impl;

import com.example.dto.CreateUserDTO;
import com.example.mapper.UserMapper;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    @Cacheable(value = "users", key = "#id")
    public Optional<User> getUserById(Long id) {
        log.info("UserServiceImpl.getUserById Inicio, parameter: id {}", id);
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al consultar usuarios por su ID: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<User> filterUsers(String name, Integer age, Pageable pageable) {
        log.info("UserServiceImpl.filterUsers Inicio parameter: name {}, age {}, pageable {}", name, age, pageable);
        try {
            if (name == null && age == null) {
                throw new IllegalArgumentException("Uno de los parámetros del filtro se tiene que rellenar");
            }

            if (name != null && age != null) {
                return userRepository.findByFirstNameAndAge(name, age, pageable);
            } else if (name != null) {
                return userRepository.findByFirstName(name, pageable);
            } else {
                return userRepository.findByAge(age, pageable);
            }
        } catch (IllegalArgumentException e) {
            log.warn("Parámetros inválidos para el filtro: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error al consultar usuarios con filtros: {}", e.getMessage(), e);
            throw new RuntimeException("Error al consultar usuarios: " + e.getMessage(), e);
        }
    }
}
