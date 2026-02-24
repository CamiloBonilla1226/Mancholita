package com.mancholita.backend.api.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoryCreateRequest {

    @NotBlank
    public String name;

    // opcional (si viene null => true)
    public Boolean active;

    // opcional:
    // null => root (Hombre/Mujer)
    // con id => child (Jeans de Hombre, etc.)
    public Long parentId;
}