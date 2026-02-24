package com.mancholita.backend.infrastructure;

import com.mancholita.backend.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActiveTrueOrderByIdDesc();

    List<Product> findByCategoryIdAndActiveTrueOrderByIdDesc(Long categoryId);

    List<Product> findByCategoryParentIdAndActiveTrueOrderByIdDesc(Long parentId);
}