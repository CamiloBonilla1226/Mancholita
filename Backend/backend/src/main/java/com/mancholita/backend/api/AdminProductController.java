package com.mancholita.backend.api;

import com.mancholita.backend.api.dto.ProductAdminDto;
import com.mancholita.backend.api.dto.ProductCreateRequest;
import com.mancholita.backend.application.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/admin/products")
    public ProductAdminDto create(@Valid @RequestBody ProductCreateRequest req) {
        return productService.create(req);
    }

    @PutMapping("/api/admin/products/{id}")
    public ProductAdminDto update(@PathVariable Long id, @Valid @RequestBody ProductCreateRequest req) {
        return productService.update(id, req);
    }
}