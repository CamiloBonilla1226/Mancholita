package com.mancholita.backend.api;

import com.mancholita.backend.api.dto.OrderCreateRequest;
import com.mancholita.backend.api.dto.OrderCreateResponse;
import com.mancholita.backend.application.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class PublicOrderController {

    private final OrderService orderService;

    public PublicOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/public/orders")
    public OrderCreateResponse create(@Valid @RequestBody OrderCreateRequest req) {
        return orderService.createPublicOrder(req);
    }
}