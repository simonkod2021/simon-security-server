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

    public Comment addComment(String blogPostId, Comment comment) {
        BlogPosts post = blogPostRepository.findById(blogPostId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        comment.setBlogPost(post);
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }
}
