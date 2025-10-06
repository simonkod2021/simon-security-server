package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "comments")
public class Comment {
    @Id
    private String id;
    private String text;
    private LocalDateTime createdAt = LocalDateTime.now();

    @DBRef
    private User author;

    @DBRef
    @JsonIgnoreProperties({"comments"})
    private BlogPosts blogPost;

    public Comment(String text, User author, BlogPosts blogPost) {
        this.text = text;
        this.author = author;
        this.blogPost = blogPost;
    }

    public Comment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public BlogPosts getBlogPost() {
        return blogPost;
    }

    public void setBlogPost(BlogPosts blogPost) {
        this.blogPost = blogPost;
    }
}
