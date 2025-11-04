package com.example.demo.dto;

import com.example.demo.models.BlogPosts;

public class BlogPostsDTO {
    private String id;
    private String title;
    private String content;
    private String author;

    public BlogPostsDTO(BlogPosts blogPosts) {
        this.id = blogPosts.getId();
        this.title = blogPosts.getTitle();
        this.content = blogPosts.getContent();
        this.author = blogPosts.getAuthor().getUsername();
    }

    public BlogPostsDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
