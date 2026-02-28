package com.mancholita.backend.infrastructure;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional; // Importante para el findById

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph; // Importante para el EntityGraph
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mancholita.backend.api.dto.OrderAdminListDto;
import com.mancholita.backend.domain.Order;

public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("""
        select new com.mancholita.backend.api.dto.OrderAdminListDto(
            o.id,
            o.createdAt,
            o.customerName,
            o.phone,
            o.total
        )
        from Order o
        where (:q is null
            or lower(o.id) like lower(concat('%', :q, '%'))
            or lower(o.customerName) like lower(concat('%', :q, '%'))
            or lower(o.phone) like lower(concat('%', :q, '%'))
        )
    """)
    Page<OrderAdminListDto> findAdminList(@Param("q") String q, Pageable pageable);

    // Para Excel por día
    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // Carga la orden y sus ítems en una sola consulta (Eager Loading)
    @EntityGraph(attributePaths = "items")
    Optional<Order> findWithItemsById(String id);
}