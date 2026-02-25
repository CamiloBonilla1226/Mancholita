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

    // ✅ PÚBLICO: filtros + búsqueda + paginación (DTO)
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

    // ✅ ADMIN: listar todo (activos o inactivos) + filtros + búsqueda + paginación (DTO)
    @Query("""
        select new com.mancholita.backend.api.dto.ProductAdminDto(
            p.id,
            p.name,
            p.description,
            p.imageUrl,
            p.price,
            p.active,
            c.id
        )
        from Product p
        join p.category c
        where (:categoryId is null or c.id = :categoryId)
          and (:active is null or p.active = :active)
          and (
                :q is null
                or lower(p.name) like lower(concat('%', :q, '%'))
                or lower(p.description) like lower(concat('%', :q, '%'))
          )
        order by p.id desc
    """)
    Page<ProductAdminDto> findAdminProducts(
            @Param("categoryId") Long categoryId,
            @Param("active") Boolean active,
            @Param("q") String q,
            Pageable pageable
    );
}