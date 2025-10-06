package com.example.demo.controllers;

import com.example.demo.Util.JwtUtil;
import com.example.demo.dto.*;
import com.example.demo.models.User;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final UserService userService;


    // Constructor Injection
    public AuthController(AuthService authService, JwtUtil jwtUtil, UserService userService) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> sign_in(@RequestBody AuthRequest authRequest) {
        return authService.authenticateUser(authRequest);
    }
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody RegisterRequest registerRequest) {
        userService.createUser(registerRequest);

        return ResponseEntity.ok("Successfully created a new user");
    }

    @PostMapping("/signout")
    public ResponseEntity<?> getUsernameFromTokenAndLogout(HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //Check if user is authenticated first
        if(authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            logger.info("User {} logged out", username);

            //Clear security context
            SecurityContextHolder.clearContext();

            //Remove jwt token from cookie
            jwtUtil.removeToken(response);
            return ResponseEntity.ok("User " + username + "logged out");
        }

        return ResponseEntity.badRequest().body("No authenticated user found");
    }


    @GetMapping("/check")
    public User check() {
        return authService.checkAuthentication();
    }

}
