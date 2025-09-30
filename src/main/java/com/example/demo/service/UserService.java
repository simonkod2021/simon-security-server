package com.example.demo.service;


import com.example.demo.dto.RegisterRequest;
import com.example.demo.models.Roles;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegisterService registerService;
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserService.class);


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RegisterService registerService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.registerService = registerService;
    }

    public void createUser(@Valid RegisterRequest registerRequest) {
        if(registerService.checkUsernameExists(registerRequest.getUsername())){
            ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        User user = new User();

        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // Om en roll är null eller helt tom sätter vi rollen till en standard roll "User"
        if(user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of(Roles.USER));
        }
        logger.info("New user registered with username: {}", registerRequest.getUsername());

        userRepository.save(user);


    }




    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

    }
    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(id));

    }

    public String getUsernameById(String id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        return user.getUsername();
    }







}
