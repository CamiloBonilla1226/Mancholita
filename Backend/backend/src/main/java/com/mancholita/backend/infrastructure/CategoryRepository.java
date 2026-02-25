package com.mancholita.backend.infrastructure;

import com.mancholita.backend.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("""
        select
            c.id as id,
            c.name as name,
            p.id as parentId
        from Category c
        left join c.parent p
        where c.active = true
        order by c.id asc
    """)
    List<CategoryRow> findActiveFlat();

    @Query("""
        select count(c) > 0
        from Category c
        where c.parent.id = :parentId
    """)
    boolean hasChildren(@Param("parentId") Long parentId);
}