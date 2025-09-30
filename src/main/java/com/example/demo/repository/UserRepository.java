package com.example.demo.repository;

import com.example.demo.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String>{

    Optional<User> findById(String id);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
}


