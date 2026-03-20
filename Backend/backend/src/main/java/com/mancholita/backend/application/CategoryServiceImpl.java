package com.mancholita.backend.application;

import com.mancholita.backend.api.dto.CategoryDto;
import com.mancholita.backend.api.dto.CategoryTreeDto;
import com.mancholita.backend.infrastructure.CategoryRepository;
import com.mancholita.backend.infrastructure.GenderCategoryRow;
import com.mancholita.backend.infrastructure.GenderRepository;
import com.mancholita.backend.infrastructure.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final GenderRepository genderRepository;
    private final ProductRepository productRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               GenderRepository genderRepository,
                               ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.genderRepository = genderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<CategoryDto> getPublicCategories() {
        return categoryRepository.findByActiveTrueOrderByNameAsc().stream()
                .map(category -> new CategoryDto(category.getId(), category.getName(), category.isActive()))
                .toList();
    }

    @Override
    public List<CategoryTreeDto> getPublicTree() {
        Map<Long, CategoryTreeDto> roots = new LinkedHashMap<>();

        genderRepository.findByActiveTrueOrderByNameAsc().forEach(gender ->
                roots.put(gender.getId(), new CategoryTreeDto(gender.getId(), gender.getName()))
        );

        List<GenderCategoryRow> rows = productRepository.findActiveGenderCategoryRows();
        for (GenderCategoryRow row : rows) {
            CategoryTreeDto root = roots.get(row.getGenderId());
            if (root == null) {
                continue;
            }
            root.children.add(new CategoryTreeDto(row.getCategoryId(), row.getCategoryName()));
        }

        return roots.values().stream()
                .peek(root -> root.children.sort((a, b) -> a.name.compareToIgnoreCase(b.name)))
                .toList();
    }
}
