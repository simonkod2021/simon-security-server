package com.example.demo.dto;

import com.example.demo.models.Comment;

public class CommentDTO {
    private String id;
    private String text;
    private String author;

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.text = comment.getText();
        this.author = comment.getAuthor().getUsername();
    }
}
