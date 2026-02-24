package com.mancholita.backend.api;

import com.mancholita.backend.api.dto.CategoryCreateRequest;
import com.mancholita.backend.domain.Category;
import com.mancholita.backend.infrastructure.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminCategoryController {

    private final CategoryRepository repo;

    public AdminCategoryController(CategoryRepository repo) {
        this.repo = repo;
    }

    // Admin: listar todas (activos e inactivos)
    @GetMapping("/api/admin/categories")
    public List<Category> listAll() {
        return repo.findAll();
    }

    // Admin: crear root o child (según parentId)
    @PostMapping("/api/admin/categories")
    public Category create(@Valid @RequestBody CategoryCreateRequest req) {
        boolean active = (req.active == null) ? true : req.active;

        Category parent = null;
        if (req.parentId != null) {
            parent = repo.findById(req.parentId)
                    .orElseThrow(() -> new IllegalArgumentException("Parent category not found: " + req.parentId));
        }

        Category category = new Category(req.name, active, parent);
        return repo.save(category);
    }

    // Admin: activar/desactivar
    @PatchMapping("/api/admin/categories/{id}/active")
    public Category setActive(@PathVariable Long id, @RequestParam boolean value) {
        Category category = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));
        category.setActive(value);
        return repo.save(category);
    }

    // Admin: renombrar (no cambia parent)
    @PutMapping("/api/admin/categories/{id}")
    public Category rename(@PathVariable Long id, @Valid @RequestBody CategoryCreateRequest req) {
        Category category = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));
        category.setName(req.name);
        return repo.save(category);
    }
}