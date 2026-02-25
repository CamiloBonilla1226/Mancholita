package com.mancholita.backend.api.dto;

import java.math.BigDecimal;

public class ProductPublicDto {

    public Long id;
    public String name;
    public String description;
    public String imageUrl;
    public BigDecimal price;
    public boolean active;

    public Long categoryId;
    public String categoryName;

    public Long genderId;
    public String genderName;

    public ProductPublicDto(
            Long id,
            String name,
            String description,
            String imageUrl,
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
        this.imageUrl = imageUrl;
        this.price = price;
        this.active = active;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.genderId = genderId;
        this.genderName = genderName;
    }
}