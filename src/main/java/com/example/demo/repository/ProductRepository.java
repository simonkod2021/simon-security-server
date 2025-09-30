package com.example.demo.repository;

import com.example.demo.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String>{

    Optional<Product> findByName(String name);
}


