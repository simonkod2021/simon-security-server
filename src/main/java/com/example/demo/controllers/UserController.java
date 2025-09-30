package com.example.demo.controllers;


import com.example.demo.Util.JwtUtil;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public UserController(UserRepository userRepository, AuthService authService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/me")
    public User getCurrentUser() {
        return authService.checkAuthentication();
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest, HttpServletResponse response) {
        try {
            User user = authService.checkAuthentication();

            if (updateUserRequest.getUsername() != null && !updateUserRequest.getUsername().isEmpty()) {
                user.setUsername(updateUserRequest.getUsername());
            }
            if (updateUserRequest.getDescription() != null && !updateUserRequest.getDescription().isEmpty()) {
                user.setDescription(updateUserRequest.getDescription());
            }
            if (updateUserRequest.getImageByte() != null && updateUserRequest.getImageByte().length > 0) {
                user.setImage(updateUserRequest.getImageByte());
            }
            if (updateUserRequest.getLocation() != null && !updateUserRequest.getLocation().isEmpty()) {
                user.setLocation(updateUserRequest.getLocation());
            }

            userRepository.save(user);
            return ResponseEntity.ok("Successfully updated user");

        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getSpecificUser(@PathVariable String id) {
      User user =  userRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
