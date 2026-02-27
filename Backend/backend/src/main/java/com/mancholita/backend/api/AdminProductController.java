package com.mancholita.backend.api;

import com.mancholita.backend.api.dto.ProductActiveRequest;
import com.mancholita.backend.api.dto.ProductAdminDto;
import com.mancholita.backend.api.dto.ProductCreateRequest;
import com.mancholita.backend.application.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // ✅ NUEVO: listar productos admin (paginado + filtros)
    @GetMapping("/api/admin/products")
    public Page<ProductAdminDto> list(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String q,
            Pageable pageable
    ) {
        return productService.listAdmin(categoryId, active, q, pageable);
    }

    @PatchMapping("/api/admin/products/{id}/active")
        public ProductAdminDto setActive(@PathVariable Long id,
                                         @Valid @RequestBody ProductActiveRequest req) {
            return productService.setActive(id, req.active);
        }

    @GetMapping("/api/admin/products/{id}")
public ProductAdminDto getById(@PathVariable Long id) {
    return productService.getById(id);
}

    @DeleteMapping("/api/admin/products/{id}")
public void delete(@PathVariable Long id) {
    productService.delete(id);
}



}