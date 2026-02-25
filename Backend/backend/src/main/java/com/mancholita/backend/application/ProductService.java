package com.mancholita.backend.application;

import com.mancholita.backend.api.dto.ProductAdminDto;
import com.mancholita.backend.api.dto.ProductCreateRequest;
import com.mancholita.backend.api.dto.ProductPublicDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductAdminDto create(ProductCreateRequest req);

    ProductAdminDto update(Long id, ProductCreateRequest req);

    Page<ProductPublicDto> listPublic(Long categoryId, Long genderId, String q, Pageable pageable);
}