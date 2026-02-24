package com.mancholita.backend.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mancholita.backend.domain.Category;
import com.mancholita.backend.infrastructure.CategoryRepository;

@RestController
public class PublicCategoryController {

    private final CategoryRepository repo;

    public PublicCategoryController(CategoryRepository repo) {
        this.repo = repo;
    }

    // 1) Raíces: Hombre / Mujer
    @GetMapping("/api/public/categories/root")
    public List<Category> roots() {
        return repo.findByParentIsNullAndActiveTrueOrderByNameAsc();
    }

    // 2) Hijas de un parent: Jeans/Camisas/Blusas según parentId
    @GetMapping("/api/public/categories/{parentId}/children")
    public List<Category> children(@PathVariable Long parentId) {
        return repo.findByParentIdAndActiveTrueOrderByNameAsc(parentId);
    }
}