package com.mancholita.backend.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mancholita.backend.api.dto.ProductPublicDto;
import com.mancholita.backend.infrastructure.ProductRepository;

@RestController
public class PublicProductController {

    private final ProductRepository productRepository;

    public PublicProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/api/public/products")
    public List<ProductPublicDto> listActive(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long parentCategoryId
    ) {
        return productRepository.findPublicProducts(categoryId, parentCategoryId);
    }
}