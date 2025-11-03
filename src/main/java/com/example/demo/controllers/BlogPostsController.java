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
    private final UserService userService;
    private final BlogPostRepository blogPostRepository;

    public BlogPostsController(BlogPostService blogPostService, UserService userService, BlogPostRepository blogPostRepository) {
        this.blogPostService = blogPostService;
        this.userService = userService;
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
    public ResponseEntity<BlogPosts> createBlogPost(@Valid @RequestBody BlogPosts blogPosts, Authentication authentication) {

        // Get the current authenticated username
        String username = authentication.getName();

        // Find the user by username and set as author
        User author = userService.findByUsername(username);

        // Set the author and other fields properly
        blogPosts.setAuthor(author);
        blogPosts.setCreatedAt(LocalDateTime.now()); // Set current timestamp

        // Save the blogpost
        BlogPosts savedBlogPost = blogPostRepository.save(blogPosts);

        // Return 200 OK or 201 CREATED, not 401!
        return ResponseEntity.ok(savedBlogPost);
        // OR: return ResponseEntity.status(HttpStatus.CREATED).body(savedBlogPost);
    }


    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBlogPost(@PathVariable String id) {
        blogPostService.deleteBlogPost(id);

        return ResponseEntity.status(401).body("You are not logged in");
    }

}

