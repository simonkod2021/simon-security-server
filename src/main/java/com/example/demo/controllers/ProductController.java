package com.example.demo.controllers;

import com.example.demo.dto.ProductRequest;
import com.example.demo.dto.ProductResponse;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/createProduct")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        ProductResponse productResponse;
            productService.createProduct(productRequest);
            productResponse = new ProductResponse(productRequest.getName(), productRequest.getPrice(), productRequest.getDescription());

        return ResponseEntity.ok(productResponse);
    }
}
