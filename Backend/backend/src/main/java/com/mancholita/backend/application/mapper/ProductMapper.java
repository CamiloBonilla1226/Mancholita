package com.mancholita.backend.application.mapper;

import com.mancholita.backend.api.dto.ProductAdminDto;
import com.mancholita.backend.domain.Product;

public final class ProductMapper {

    private ProductMapper() {}

    public static ProductAdminDto toAdminDto(Product p) {
        return new ProductAdminDto(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getImageUrl(),
                p.getPrice(),
                p.isActive(),
                p.getCategory().getId()
        );
    }
}