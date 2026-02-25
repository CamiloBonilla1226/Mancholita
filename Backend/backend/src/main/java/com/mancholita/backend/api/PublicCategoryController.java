package com.mancholita.backend.api;

import com.mancholita.backend.api.dto.CategoryTreeDto;
import com.mancholita.backend.application.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PublicCategoryController {

    private final CategoryService categoryService;

    public PublicCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/api/public/categories/tree")
    public List<CategoryTreeDto> tree() {
        return categoryService.getPublicTree();
    }
}