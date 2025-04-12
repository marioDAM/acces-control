package controller;

import com.example.Main;
import com.example.controller.UserController;
import com.example.dto.CreateUserDTO;
import com.example.model.User;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void createUser_ShouldReturnCreatedUser() {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setFirstName("John");
        createUserDTO.setLastName("Doe");
        createUserDTO.setAge(30);

        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setAge(30);

        when(userService.createUser(createUserDTO)).thenReturn(user);

        ResponseEntity<User> response = userController.createUser(createUserDTO);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstName());
        verify(userService, times(1)).createUser(createUserDTO);
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setAge(30);

        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserById(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstName());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserDoesNotExist() {
        Long userId = 1L;

        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> userController.getUserById(userId));
        assertEquals("User not found with id: " + userId, exception.getMessage());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void filterUsers_ShouldReturnFilteredUsers() {
        // Arrange
        String name = "John";
        Integer age = 30;

        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setAge(30);

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("John");
        user2.setLastName("Smith");
        user2.setAge(30);

        List<User> filteredUsers = Arrays.asList(user1, user2);

        when(userService.filterUsers(name, age)).thenReturn(filteredUsers);

        ResponseEntity<List<User>> response = userController.filterUsers(name, age);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).filterUsers(name, age);
    }
}
