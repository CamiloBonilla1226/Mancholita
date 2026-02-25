package com.mancholita.backend.api;

import com.mancholita.backend.api.dto.ProductCreateRequest;
import com.mancholita.backend.domain.Category;
import com.mancholita.backend.domain.Product;
import com.mancholita.backend.infrastructure.CategoryRepository;
import com.mancholita.backend.infrastructure.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public AdminProductController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/api/admin/products")
    public Product create(@Valid @RequestBody ProductCreateRequest req) {
        boolean active = (req.active == null) ? true : req.active;

        Category category = categoryRepository.findById(req.categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + req.categoryId));

        Product product = new Product(
                req.name,
                req.description,
                req.price,
                active,
                req.imageUrl,
                category
        );

        return productRepository.save(product);
    }

    @PutMapping("/api/admin/products/{id}")
    public Product update(@PathVariable Long id, @Valid @RequestBody ProductCreateRequest req) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        Category category = categoryRepository.findById(req.categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + req.categoryId));

        product.update(
                req.name,
                req.description,
                req.price,
                req.imageUrl,
                category
        );

        return productRepository.save(product);
    }
}