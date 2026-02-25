package com.mancholita.backend.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mancholita.backend.api.dto.ProductPublicDto;
import com.mancholita.backend.infrastructure.ProductRepository;

@RestController
public class PublicProductController {

    private final ProductRepository repo;

    public PublicProductController(ProductRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/api/public/products")
public Page<ProductPublicDto> list(
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) Long genderId,
        Pageable pageable
) {
    return repo.findPublicProducts(categoryId, genderId, pageable);
}
}