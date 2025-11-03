package com.example.demo.service;

import com.example.demo.models.BlogPosts;
import com.example.demo.models.Comment;
import com.example.demo.repository.BlogPostRepository;
import com.example.demo.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BlogPostRepository blogPostRepository;

    public CommentService(CommentRepository commentRepository, BlogPostRepository blogPostRepository) {
        this.commentRepository = commentRepository;
        this.blogPostRepository = blogPostRepository;
    }

    public List<Comment> getCommentsByBlogPostId(String blogPostId) {
        return commentRepository.findByBlogPost_Id(blogPostId);
    }

    public void addComment(String id, Comment comment) {
        BlogPosts post = blogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        comment.setBlogPost(post);
        comment.setCreatedAt(LocalDateTime.now());
        System.out.println("Service - Comment text: " + comment.getText());
        System.out.println("Service - BlogPost: " + post.getId());
        commentRepository.save(comment);
        System.out.println("Comment saved with ID: " + comment.getId());
    }

    // Return all comments in the database
    public List<Comment> getAllComments(){
        return commentRepository.findAll();
    }
}
