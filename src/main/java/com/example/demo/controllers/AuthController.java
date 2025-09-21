package com.example.demo.controllers;

import com.example.demo.Util.JwtUtil;
import com.example.demo.dto.*;
import com.example.demo.models.Roles;
import com.example.demo.models.User;
import com.example.demo.service.AuthService;
import com.example.demo.service.RegisterService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@ControllerAdvice
@RequestMapping("/api/auth")
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RegisterService registerService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;


    // Constructor Injection
    public AuthController(PasswordEncoder passwordEncoder, UserService userService, RegisterService registerService, AuthService authService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.registerService = registerService;
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> sign_in(@RequestBody User user) {
        AuthResponse response = authService.authenticateUser(user);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody RegisterRequest registerRequest) {

        if(registerService.checkUsernameExists(registerRequest.getUsername())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        // Om en roll är null eller helt tom sätter vi rollen till en standard roll "User"
            if(user.getRoles() == null || user.getRoles().isEmpty()) {
               user.setRoles(Set.of(Roles.USER));
            }
        userService.createUser(user);
        logger.info("New user registered with username: {}", registerRequest.getUsername());

        RegisterResponse registerResponse = new RegisterResponse(userService.findByUsername(user.getUsername()).getId(),"Successfully created a new user", user.getUsername(), user.getRoles());

        return ResponseEntity.ok(registerResponse);

    }
}
