package com.example.demo.repository;

import com.example.demo.models.BlogPosts;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogPostRepository extends MongoRepository<BlogPosts, String> {}
