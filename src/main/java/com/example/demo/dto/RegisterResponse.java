package com.example.demo.dto;

import com.example.demo.models.Roles;

import java.util.Set;

public class RegisterResponse {
    private String id;

    private String message;

    private String username;

    private Set<Roles> roles;

    public RegisterResponse(String id, String message, String username, Set<Roles> roles) {
        this.id = id;
        this.message = message;
        this.username = username;
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Set<Roles> roles) {
        this.roles = roles;
    }
}
