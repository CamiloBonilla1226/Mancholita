package com.mancholita.backend.api;

import com.mancholita.backend.api.dto.CategoryDto;
import com.mancholita.backend.api.dto.CategoryTreeDto;
import com.mancholita.backend.application.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/categories")
public class PublicCategoryController {

    private final CategoryService categoryService;

    public PublicCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDto> list() {
        return categoryService.getPublicCategories();
    }

    @GetMapping("/tree")
    public List<CategoryTreeDto> tree() {
        return categoryService.getPublicTree();
    }
}
