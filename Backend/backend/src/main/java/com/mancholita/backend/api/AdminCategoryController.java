package com.mancholita.backend.api;

import com.mancholita.backend.api.dto.CategoryCreateRequest;
import com.mancholita.backend.api.dto.CategoryDto;
import com.mancholita.backend.domain.Category;
import com.mancholita.backend.infrastructure.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

    private final CategoryRepository repo;

    public AdminCategoryController(CategoryRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<CategoryDto> listAll() {
        return repo.findAllByOrderByNameAsc().stream()
                .map(this::toDto)
                .toList();
    }

    @PostMapping
    public CategoryDto create(@Valid @RequestBody CategoryCreateRequest req) {
        repo.findByNameIgnoreCase(req.name.trim())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Category already exists: " + req.name.trim());
                });

        boolean active = (req.active == null) ? true : req.active;
        Category category = new Category(req.name.trim(), active);
        return toDto(repo.save(category));
    }

    @PatchMapping("/{id}/active")
    public CategoryDto setActive(@PathVariable Long id, @RequestParam boolean value) {
        Category category = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));
        category.setActive(value);
        return toDto(repo.save(category));
    }

    @PutMapping("/{id}")
    public CategoryDto rename(@PathVariable Long id, @Valid @RequestBody CategoryCreateRequest req) {
        Category category = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));

        repo.findByNameIgnoreCase(req.name.trim())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Category already exists: " + req.name.trim());
                });

        category.setName(req.name.trim());
        if (req.active != null) {
            category.setActive(req.active);
        }
        return toDto(repo.save(category));
    }

    private CategoryDto toDto(Category category) {
        return new CategoryDto(category.getId(), category.getName(), category.isActive());
    }
}
