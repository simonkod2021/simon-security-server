package com.example.demo.controllers;

import com.example.demo.dto.CommentDTO;
import com.example.demo.models.BlogPosts;
import com.example.demo.models.Comment;
import com.example.demo.models.User;
import com.example.demo.service.AuthService;
import com.example.demo.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/auth/comments")
public class CommentsController {

    private final CommentService commentService;
    private final AuthService authService;

    public CommentsController(CommentService commentService, AuthService authService) {
        this.commentService = commentService;
        this.authService = authService;
    }

    // Get all comments on a specific blogpost
    @GetMapping("/{id}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable String id){
        return ResponseEntity.ok(commentService.getCommentsByBlogPostId(id));
    }

    // Add a new comment to a blogpost
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add/{id}")
    public ResponseEntity<?> addComment(@PathVariable String id, @RequestBody CommentDTO commentDTO){
        User user = authService.checkAuthentication();
        if (user == null) {
            return ResponseEntity.status(401).body("Please log in to add a comment");
        }

        Comment comment = new Comment();
        comment.setText(commentDTO.getText());
        comment.setAuthor(user);
        comment.setCreatedAt(LocalDateTime.now());
        commentService.addComment(id, comment);

        return ResponseEntity.ok("Comment added successfully");
    }

    // Get all comments from the DB
    @GetMapping(value = "/all")
    public List<Comment> getComments(){
        return commentService.getAllComments();
    }
}
