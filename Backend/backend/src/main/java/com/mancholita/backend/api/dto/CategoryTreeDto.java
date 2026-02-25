package com.mancholita.backend.api.dto;

import java.util.ArrayList;
import java.util.List;

public class CategoryTreeDto {

    public Long id;
    public String name;
    public Long parentId;
    public List<CategoryTreeDto> children = new ArrayList<>();

    public CategoryTreeDto(Long id, String name, Long parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }
}