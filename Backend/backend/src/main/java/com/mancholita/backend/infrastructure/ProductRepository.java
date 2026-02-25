package com.mancholita.backend.infrastructure;

import com.mancholita.backend.api.dto.ProductPublicDto;
import com.mancholita.backend.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        select new com.mancholita.backend.api.dto.ProductPublicDto(
            p.id,
            p.name,
            p.description,
            p.imageUrl,
            p.price,
            p.active,
            c.id,
            c.name,
            parent.id,
            parent.name
        )
        from Product p
        join p.category c
        left join c.parent parent
        where p.active = true
          and (:categoryId is null or c.id = :categoryId)
          and (:genderId is null or parent.id = :genderId)
        order by p.id desc
    """)
    Page<ProductPublicDto> findPublicProducts(
            @Param("categoryId") Long categoryId,
            @Param("genderId") Long genderId,
            Pageable pageable
    );
}