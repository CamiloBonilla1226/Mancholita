package com.mancholita.backend.application;

import com.mancholita.backend.api.dto.ProductAdminDto;
import com.mancholita.backend.api.dto.ProductCreateRequest;
import com.mancholita.backend.api.dto.ProductPublicDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductAdminDto create(ProductCreateRequest req);

    ProductAdminDto update(Long id, ProductCreateRequest req);

    Page<ProductAdminDto> listAdmin(Long categoryId, Long genderId, Boolean active, String q, Pageable pageable);

    ProductAdminDto setActive(Long id, boolean active);

    ProductAdminDto getById(Long id);

    void delete(Long id);

    Page<ProductPublicDto> listPublic(Long categoryId, Long genderId, String q, Pageable pageable);
}
