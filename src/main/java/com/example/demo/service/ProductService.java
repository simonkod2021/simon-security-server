package com.example.demo.service;

import com.example.demo.dto.ProductRequest;
import com.example.demo.models.Product;
import com.example.demo.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;


@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void createProduct(@Valid ProductRequest productRequest){

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        productRepository.save(product);
    }

}
