package com.example.demo.controllers;


import com.example.demo.dto.BlogPostsDTO;
import com.example.demo.models.BlogPosts;
import com.example.demo.models.Comment;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.BlogPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/blogposts")
public class BlogPostsController {

    private final BlogPostService blogPostService;
    private final AuthService authService;

    public BlogPostsController(BlogPostService blogPostService, AuthService authService) {
        this.blogPostService = blogPostService;
        this.authService = authService;
    }
    @GetMapping(value = "/all")
    public List<BlogPosts> getBlogPosts(){
        return blogPostService.getAllBlogPosts();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<BlogPostsDTO> getBlogPostById(@PathVariable String id){
        return ResponseEntity.ok(blogPostService.getBlogPostWithComments(id));
    }

    @PostMapping(value = "/create")
    public ResponseEntity<BlogPosts> createBlogPost(@RequestBody BlogPosts blogPosts) {
        User user = authService.checkAuthentication();
        if (user == null) {
            return ResponseEntity.status(401).body(blogPosts);
        } else {
            blogPosts.setAuthor(user);
            return ResponseEntity.status(201).body(blogPostService.createPost(blogPosts));
        }
    }

        @PostMapping(value = "/{id}/comment")
        public ResponseEntity<Comment> addComment(@PathVariable String id, @RequestBody Comment comment){
            User user = authService.checkAuthentication();
            if (user == null) {
                return ResponseEntity.status(401).body(comment);
            } else {
                comment.setAuthor(user);
                return ResponseEntity.status(201).body(blogPostService.addComment(id, comment));
        }
    }
}
