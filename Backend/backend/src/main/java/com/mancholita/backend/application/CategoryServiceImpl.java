package com.mancholita.backend.application;

import com.mancholita.backend.api.dto.CategoryTreeDto;
import com.mancholita.backend.infrastructure.CategoryRepository;
import com.mancholita.backend.infrastructure.CategoryRow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryTreeDto> getPublicTree() {

        List<CategoryRow> rows = categoryRepository.findActiveFlat();

        Map<Long, CategoryTreeDto> nodes = new LinkedHashMap<>();
        for (CategoryRow r : rows) {
            nodes.put(
                    r.getId(),
                    new CategoryTreeDto(r.getId(), r.getName(), r.getParentId())
            );
        }

        List<CategoryTreeDto> roots = new ArrayList<>();
        for (CategoryTreeDto node : nodes.values()) {
            if (node.parentId == null) {
                roots.add(node);
            } else {
                CategoryTreeDto parent = nodes.get(node.parentId);
                if (parent != null) parent.children.add(node);
            }
        }

        // Orden amigable para UI
        for (CategoryTreeDto root : roots) {
            root.children.sort(Comparator.comparing(a -> a.name.toLowerCase()));
        }
        roots.sort(Comparator.comparing(a -> a.name.toLowerCase()));

        return roots;
    }
}