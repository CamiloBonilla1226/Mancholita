package com.mancholita.backend.api.dto;

import java.math.BigDecimal;

public class ProductPublicDto {

    public Long id;
    public String name;
    public String description;
    public BigDecimal price;
    public boolean active;

    public Long categoryId;
    public String categoryName;

    public Long genderId;      // Hombre(1) / Mujer(2)
    public String genderName;  // "Hombre" / "Mujer"

    public ProductPublicDto(
            Long id,
            String name,
            String description,
            BigDecimal price,
            boolean active,
            Long categoryId,
            String categoryName,
            Long genderId,
            String genderName
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.active = active;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.genderId = genderId;
        this.genderName = genderName;
    }
}