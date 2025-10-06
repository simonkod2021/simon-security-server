package com.example.demo.dto;

import com.example.demo.models.BlogPosts;

import java.util.List;

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
}
