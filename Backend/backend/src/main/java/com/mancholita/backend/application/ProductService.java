package com.mancholita.backend.application;

import com.mancholita.backend.api.dto.ProductCreateRequest;
import com.mancholita.backend.api.dto.ProductPublicDto;
import com.mancholita.backend.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Product create(ProductCreateRequest req);

    Product update(Long id, ProductCreateRequest req);

    Page<ProductPublicDto> listPublic(Long categoryId, Long genderId, String q, Pageable pageable);
}