package com.mancholita.backend.infrastructure;

import com.mancholita.backend.api.dto.ProductPublicDto;
import com.mancholita.backend.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        select new com.mancholita.backend.api.dto.ProductPublicDto(
            p.id,
            p.name,
            p.description,
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
          and (:parentCategoryId is null or parent.id = :parentCategoryId)
        order by p.id desc
    """)
    List<ProductPublicDto> findPublicProducts(
            @Param("categoryId") Long categoryId,
            @Param("parentCategoryId") Long parentCategoryId
    );
}