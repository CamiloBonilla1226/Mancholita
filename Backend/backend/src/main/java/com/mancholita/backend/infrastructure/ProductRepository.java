package com.mancholita.backend.infrastructure;

import com.mancholita.backend.api.dto.ProductAdminDto;
import com.mancholita.backend.api.dto.ProductPublicDto;
import com.mancholita.backend.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        select distinct
            g.id as genderId,
            g.name as genderName,
            c.id as categoryId,
            c.name as categoryName
        from Product p
        join p.category c
        join p.gender g
        where p.active = true
          and c.active = true
          and g.active = true
        order by g.name asc, c.name asc
    """)
    java.util.List<GenderCategoryRow> findActiveGenderCategoryRows();

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
            g.id,
            g.name
        )
        from Product p
        join p.category c
        join p.gender g
        where p.active = true
          and c.active = true
          and g.active = true
          and (:categoryId is null or c.id = :categoryId)
          and (:genderId is null or g.id = :genderId)
          and (
                :q is null
                or lower(p.name) like lower(concat('%', :q, '%'))
                or lower(p.description) like lower(concat('%', :q, '%'))
          )
        order by p.id desc
    """)
    Page<ProductPublicDto> findPublicProducts(
            @Param("categoryId") Long categoryId,
            @Param("genderId") Long genderId,
            @Param("q") String q,
            Pageable pageable
    );

    @Query("""
        select new com.mancholita.backend.api.dto.ProductAdminDto(
            p.id,
            p.name,
            p.description,
            p.imageUrl,
            p.price,
            p.active,
            c.id,
            c.name,
            g.id,
            g.name
        )
        from Product p
        join p.category c
        join p.gender g
        where (:categoryId is null or c.id = :categoryId)
          and (:genderId is null or g.id = :genderId)
          and (:active is null or p.active = :active)
          and (
                :q is null
                or lower(p.name) like lower(concat('%', :q, '%'))
                or lower(p.description) like lower(concat('%', :q, '%'))
          )
    """)
    Page<ProductAdminDto> findAdminProducts(
            @Param("categoryId") Long categoryId,
            @Param("genderId") Long genderId,
            @Param("active") Boolean active,
            @Param("q") String q,
            Pageable pageable
    );
}
