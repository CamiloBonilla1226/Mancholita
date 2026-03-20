package com.mancholita.backend.application;

import com.mancholita.backend.api.dto.CategoryDto;
import com.mancholita.backend.api.dto.CategoryTreeDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getPublicCategories();

    List<CategoryTreeDto> getPublicTree();
}
