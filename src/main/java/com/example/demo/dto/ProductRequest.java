package com.example.demo.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ProductRequest {
    @Size(min = 3, max = 20)
    private String name;

    @Size(min = 10, max = 100)
    private String description;

    @DecimalMin(value = "10", message = "Price must be greater than 10")
    @DecimalMax(value = "10000", message = "Price must be less than 10000")
    private BigDecimal price;

    public ProductRequest(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
    public ProductRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

