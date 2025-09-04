package com.example.demo.dto;

import com.example.demo.models.Roles;

import java.util.Set;

public class AuthResponse {
    private String id;
    private String jwtToken;
    private String username;
    private Set<Roles> roles;

    public AuthResponse(String id, String jwtToken, String username, Set<Roles> roles) {
        this.id = id;
        this.jwtToken = jwtToken;
        this.username = username;
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
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
