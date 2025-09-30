package com.example.demo.service;

import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class RegisterService {
    private final UserRepository userRepository;

    public RegisterService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public boolean checkUsernameExists(String username){
        return userRepository.findById(username).isPresent();
    }
}
