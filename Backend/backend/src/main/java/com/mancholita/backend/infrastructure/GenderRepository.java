package com.mancholita.backend.infrastructure;

import com.mancholita.backend.domain.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GenderRepository extends JpaRepository<Gender, Long> {

    List<Gender> findAllByOrderByNameAsc();

    List<Gender> findByActiveTrueOrderByNameAsc();

    Optional<Gender> findByNameIgnoreCase(String name);
}
