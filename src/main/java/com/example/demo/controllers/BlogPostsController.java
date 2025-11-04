package com.example.demo.controllers;


import com.example.demo.dto.BlogPostsDTO;
import com.example.demo.models.BlogPosts;
import com.example.demo.models.User;
import com.example.demo.repository.BlogPostRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.BlogPostService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/auth/blogposts")
public class BlogPostsController {

    private final BlogPostService blogPostService;
    private final BlogPostRepository blogPostRepository;

    public BlogPostsController(BlogPostService blogPostService, BlogPostRepository blogPostRepository) {
        this.blogPostService = blogPostService;
        this.blogPostRepository = blogPostRepository;
    }

    // Get all blogposts from the DB
    @GetMapping(value = "/all")
    public List<BlogPosts> getBlogPosts() {
        return blogPostService.getAllBlogPosts();
    }

    // Get a specific blogpost by id from DB
    @GetMapping(value = "/{id}")
    public ResponseEntity<BlogPostsDTO> getBlogPostById(@PathVariable String id) {
        return ResponseEntity.ok(blogPostService.getBlogPostWithComments(id));
    }

    // Create a new blogpost
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/create")
    public ResponseEntity<BlogPosts> createBlogPost(@Valid @RequestBody BlogPostsDTO blogPostsDTO, Authentication authentication) {
            blogPostService.createPost(blogPostsDTO, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBlogPost(@PathVariable String id) {
        blogPostService.deleteBlogPost(id);

        return ResponseEntity.status(401).body("You are not logged in");
    }

}

