package com.mancholita.backend.api.dto;

import java.math.BigDecimal;

public class ProductAdminDto {

    public Long id;
    public String name;
    public String description;
    public String imageUrl;
    public BigDecimal price;
    public boolean active;

    public Long categoryId;

    public ProductAdminDto(Long id, String name, String description, String imageUrl,
                           BigDecimal price, boolean active, Long categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.active = active;
        this.categoryId = categoryId;
    }
}