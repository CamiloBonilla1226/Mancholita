package com.mancholita.backend.api;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mancholita.backend.api.dto.CategoryTreeDto;
import com.mancholita.backend.infrastructure.CategoryRepository;
import com.mancholita.backend.infrastructure.CategoryRow;

@RestController
public class PublicCategoryController {

    private final CategoryRepository repo;

    public PublicCategoryController(CategoryRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/api/public/categories/tree")
    public List<CategoryTreeDto> tree() {

        List<CategoryRow> rows = repo.findActiveFlat();

        // id -> nodo
        Map<Long, CategoryTreeDto> nodes = new LinkedHashMap<>();
        for (CategoryRow r : rows) {
            nodes.put(r.getId(), new CategoryTreeDto(r.getId(), r.getName(), r.getParentId()));
        }

        // armar jerarquía
        List<CategoryTreeDto> roots = new ArrayList<>();
        for (CategoryTreeDto n : nodes.values()) {
            if (n.parentId == null) {
                roots.add(n);
            } else {
                CategoryTreeDto parent = nodes.get(n.parentId);
                if (parent != null) parent.children.add(n);
            }
        }

        // opcional: ordenar children por name
        for (CategoryTreeDto root : roots) {
            root.children.sort(Comparator.comparing(a -> a.name.toLowerCase()));
        }
        roots.sort(Comparator.comparing(a -> a.name.toLowerCase()));

        return roots;
    }
}