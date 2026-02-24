package com.mancholita.backend.api;

import com.mancholita.backend.domain.Product;
import com.mancholita.backend.infrastructure.ProductRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PublicProductController {

    private final ProductRepository productRepository;

    public PublicProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/api/public/products")
    public List<Product> listActive() {
        return productRepository.findByActiveTrueOrderByIdDesc();
    }
}