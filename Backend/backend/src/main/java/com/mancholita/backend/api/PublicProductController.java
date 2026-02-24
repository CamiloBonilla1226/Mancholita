package com.mancholita.backend.api;

import com.mancholita.backend.domain.Product;
import com.mancholita.backend.infrastructure.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PublicProductController {

    private final ProductRepository productRepository;

    public PublicProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/api/public/products")
    public List<Product> listActive(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long parentCategoryId
    ) {

        // 🔹 Filtrar por subcategoría (Jeans, Camisas, etc.)
        if (categoryId != null) {
            return productRepository
                    .findByCategoryIdAndActiveTrueOrderByIdDesc(categoryId);
        }

        // 🔹 Filtrar por categoría raíz (Hombre/Mujer)
        if (parentCategoryId != null) {
            return productRepository
                    .findByCategoryParentIdAndActiveTrueOrderByIdDesc(parentCategoryId);
        }

        // 🔹 Todos los activos
        return productRepository.findByActiveTrueOrderByIdDesc();
    }
}