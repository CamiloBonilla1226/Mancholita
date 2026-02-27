package com.mancholita.backend.infrastructure;

import com.mancholita.backend.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {}