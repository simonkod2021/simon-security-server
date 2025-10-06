package com.example.demo.controllers;

import com.example.demo.models.Comment;
import com.example.demo.models.User;
import com.example.demo.service.AuthService;
import com.example.demo.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable String id){
        return ResponseEntity.ok(commentService.getCommentsByBlogPostId(id));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add/{id}")
    public ResponseEntity<?> addComment(@PathVariable String id, @RequestBody Comment comment){
        User user = authService.checkAuthentication();
        if (user == null) {
            return ResponseEntity.status(401).body("Please log in to add a comment");
        }

        comment.setAuthor(user);
        commentService.addComment(id, comment);
        return ResponseEntity.ok("Comment added successfully");
    }
}
