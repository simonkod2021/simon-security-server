package com.example.demo.service;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.models.Roles;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RegisterService registerService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_successfulRegistration() {
        RegisterRequest request = new RegisterRequest("newUser","password123" );
        request.setUsername("newUser");
        request.setPassword("password123");

        when(registerService.checkUsernameExists("newUser")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        userService.createUser(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getUsername()).isEqualTo("newUser");
        assertThat(savedUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(savedUser.getRoles()).containsExactly(Roles.USER);
    }

    @Test
    void createUser_usernameAlreadyExists() {
        RegisterRequest request = new RegisterRequest("newUser","password123");
        request.setUsername("existingUser");
        request.setPassword("password123");

        when(registerService.checkUsernameExists("existingUser")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(request));

        verify(userRepository, never()).save(any());
    }

    // ------------------ findByUsername ------------------

    @Test
    void findByUsername_userExists() {
        User user = new User();
        user.setUsername("existingUser");

        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(user));

        User result = userService.findByUsername("existingUser");

        assertThat(result).isEqualTo(user);
    }

    @Test
    void findByUsername_userDoesNotExist() {
        when(userRepository.findByUsername("missingUser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.findByUsername("missingUser"));
    }

    // ------------------ findById ------------------

    @Test
    void findById_userExists() {
        User user = new User();
        user.setId("123");
        user.setUsername("testUser");

        when(userRepository.findById("123")).thenReturn(Optional.of(user));

        User result = userService.findById("123");

        assertThat(result).isEqualTo(user);
    }

    @Test
    void findById_userDoesNotExist() {
        when(userRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.findById("999"));
    }

    // ------------------ getUsernameById ------------------

    @Test
    void getUsernameById_userExists() {
        User user = new User();
        user.setId("123");
        user.setUsername("testUser");

        when(userRepository.findById("123")).thenReturn(Optional.of(user));

        String username = userService.getUsernameById("123");

        assertThat(username).isEqualTo("testUser");
    }

    @Test
    void getUsernameById_userDoesNotExist() {
        when(userRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getUsernameById("999"));
    }
}