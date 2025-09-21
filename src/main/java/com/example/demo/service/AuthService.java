package com.example.demo.service;

import com.example.demo.Util.JwtUtil;
import com.example.demo.dto.AuthResponse;
import com.example.demo.models.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    public AuthResponse authenticateUser(User user) {
        Authentication authentication = authenticationManager.authenticate(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails.getUsername(), user.getRoles());
        if(user.getRoles() == null || user.getRoles().isEmpty())
            user.setRoles(userService.findByUsername(user.getUsername()).getRoles());
        return new AuthResponse(userService.findByUsername(user.getUsername()).getId(), user.getRoles(), user.getUsername(), "Succesfully logged in!");
    }
}
