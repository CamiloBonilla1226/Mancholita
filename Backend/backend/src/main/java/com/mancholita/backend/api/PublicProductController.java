package com.mancholita.backend.api;

import com.mancholita.backend.api.dto.ProductPublicDto;
import com.mancholita.backend.application.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
public class PublicProductController {

    private final ProductService productService;

    public PublicProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/api/public/products")
    public Page<ProductPublicDto> list(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long genderId,
            @RequestParam(required = false) String q,
            Pageable pageable
    ) {
        return productService.listPublic(categoryId, genderId, q, pageable);
    }
}