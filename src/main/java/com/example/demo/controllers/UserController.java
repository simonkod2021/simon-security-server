package com.example.demo.controllers;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.models.Roles;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword());

        userService.createUser(user);

        UserResponse userResponse = new UserResponse("Successfully created user",
                user.getUsername(),
                user.getRoles()
        );

        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);




    }
}
