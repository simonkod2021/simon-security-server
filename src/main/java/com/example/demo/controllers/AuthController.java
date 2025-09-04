package com.example.demo.controllers;
import com.example.demo.Util.JwtUtil;
import com.example.demo.dto.*;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.RegisterService;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final RegisterService registerService;

    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, UserService userService, RegisterService registerService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.registerService = registerService;
    }
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        jwtUtil.generateToken(String.valueOf(userDetails));
        AuthResponse authResponse = new AuthResponse(userService.findByUsername(user.getUsername()).getId(),"Successfully logged in", user.getUsername(), userService.findByUsername(user.getUsername()).getRoles());
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegisterRequest registerRequest) {

        if(registerService.checkUsernameExists(registerRequest.getUsername())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        if(!registerService.checkUsernameExists(registerRequest.getUsername()))

            userService.createUser(user);

        RegisterResponse registerResponse = new RegisterResponse(userService.findByUsername(user.getUsername()).getId(),"Successfully created a new user", user.getUsername(), user.getRoles());

        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);

    }
}
