package com.mancholita.backend.application;

import com.mancholita.backend.api.dto.ProductAdminDto;
import com.mancholita.backend.api.dto.ProductCreateRequest;
import com.mancholita.backend.api.dto.ProductPublicDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    // Admin
    ProductAdminDto create(ProductCreateRequest req);

    ProductAdminDto update(Long id, ProductCreateRequest req);

    Page<ProductAdminDto> listAdmin(Long categoryId, Boolean active, String q, Pageable pageable);

    ProductAdminDto setActive(Long id, boolean active);

    // Public
    Page<ProductPublicDto> listPublic(Long categoryId, Long genderId, String q, Pageable pageable);

    
}