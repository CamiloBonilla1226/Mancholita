package com.mancholita.backend.infrastructure;

import com.mancholita.backend.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Raíces: Hombre, Mujer
    List<Category> findByParentIsNullAndActiveTrueOrderByNameAsc();

    // Hijas por padre: Jeans/Camisas de Hombre, etc.
    List<Category> findByParentIdAndActiveTrueOrderByNameAsc(Long parentId);
}