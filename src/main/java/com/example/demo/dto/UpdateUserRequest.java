package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateUserRequest {


//    @Size(min = 4, max = 15)
    private String username;


//    @Size(min = 4, max = 100)
    private String description;

    private byte[] imageByte;


//    @Size(min = 4, max = 20)
    private String location;

    public UpdateUserRequest(String username, String description, byte[] imageByte, String location) {
        this.username = username;
        this.description = description;
        this.imageByte = imageByte;
        this.location = location;
    }

    public UpdateUserRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImageByte() {
        return imageByte;
    }

    public void setImageByte(byte[] imageByte) {
        this.imageByte = imageByte;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
