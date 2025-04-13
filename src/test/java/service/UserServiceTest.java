package service;

import com.example.dto.CreateUserDTO;
import com.example.mapper.UserMapper;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUserOk() {
        CreateUserDTO dto = new CreateUserDTO();
        User user = new User();
        when(userMapper.toEntity(dto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(dto);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userMapper, times(1)).toEntity(dto);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUserKO() {
        CreateUserDTO dto = new CreateUserDTO();
        User user = new User();
        when(userMapper.toEntity(dto)).thenReturn(user);
        when(userRepository.save(user)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.createUser(dto));
        assertTrue(exception.getMessage().contains("Error al crear usuario"));
        verify(userMapper, times(1)).toEntity(dto);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getUserByIdOK() {
        Long userId = 1L;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(userId);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserByIdKO() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserById(userId));
        assertTrue(exception.getMessage().contains("Error al consultar usuarios por su ID"));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void filterUsersOK() {
        String name = "John";
        Integer age = 30;
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = List.of(new User());
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());

        when(userRepository.findByFirstNameAndAge(name, age, pageable)).thenReturn(userPage);

        Page<User> result = userService.filterUsers(name, age, pageable);

        assertNotNull(result);
        assertEquals(users.size(), result.getTotalElements());
        verify(userRepository, times(1)).findByFirstNameAndAge(name, age, pageable);
    }

    @Test
    void filterUsers_ShouldReturnUsers_WhenOnlyNameProvided() {
        String name = "John";
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = List.of(new User());
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());

        when(userRepository.findByFirstName(name, pageable)).thenReturn(userPage);

        Page<User> result = userService.filterUsers(name, null, pageable);

        assertNotNull(result);
        assertEquals(users.size(), result.getTotalElements());
        verify(userRepository, times(1)).findByFirstName(name, pageable);
    }

    @Test
    void filterUsers_ShouldReturnUsers_WhenOnlyAgeProvided() {
        Integer age = 30;
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = List.of(new User());
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());

        when(userRepository.findByAge(age, pageable)).thenReturn(userPage);

        Page<User> result = userService.filterUsers(null, age, pageable);

        assertNotNull(result);
        assertEquals(users.size(), result.getTotalElements());
        verify(userRepository, times(1)).findByAge(age, pageable);
    }

    @Test
    void filterUsers_No_Filters_KO() {
        Pageable pageable = PageRequest.of(0, 10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.filterUsers(null, null, pageable));
        assertEquals("Uno de los parámetros del filtro se tiene que rellenar", exception.getMessage());
        verifyNoInteractions(userRepository);
    }

    @Test
    void filterUsers_KO() {
        String name = "John";
        Integer age = 30;
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.findByFirstNameAndAge(name, age, pageable)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.filterUsers(name, age, pageable));
        assertTrue(exception.getMessage().contains("Error al consultar usuarios"));
        verify(userRepository, times(1)).findByFirstNameAndAge(name, age, pageable);
    }
}
