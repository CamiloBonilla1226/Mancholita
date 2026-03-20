package com.mancholita.backend.api.dto;

public class CategoryDto {

    public Long id;
    public String name;
    public boolean active;

    public CategoryDto(Long id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }
}
