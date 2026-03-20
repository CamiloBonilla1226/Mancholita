package com.mancholita.backend.api;

import com.mancholita.backend.api.dto.ProductActiveRequest;
import com.mancholita.backend.api.dto.ProductAdminDto;
import com.mancholita.backend.api.dto.ProductCreateRequest;
import com.mancholita.backend.application.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ProductAdminDto create(@Valid @RequestBody ProductCreateRequest req) {
        return productService.create(req);
    }

    @PutMapping("/{id}")
    public ProductAdminDto update(@PathVariable Long id, @Valid @RequestBody ProductCreateRequest req) {
        return productService.update(id, req);
    }

    @GetMapping
    public Page<ProductAdminDto> list(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long genderId,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String q,
            Pageable pageable
    ) {
        return productService.listAdmin(categoryId, genderId, active, q, pageable);
    }

    @PatchMapping("/{id}/active")
    public ProductAdminDto setActive(@PathVariable Long id,
                                     @Valid @RequestBody ProductActiveRequest req) {
        return productService.setActive(id, req.active);
    }

    @GetMapping("/{id}")
    public ProductAdminDto getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}
