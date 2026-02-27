package com.mancholita.backend.api;

import com.mancholita.backend.api.dto.OrderAdminDetailDto;
import com.mancholita.backend.api.dto.OrderAdminDto;
import com.mancholita.backend.application.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/api/admin/orders")
    public Page<OrderAdminDto> list(Pageable pageable) {
        return orderService.listAdmin(pageable);
    }

    @GetMapping("/api/admin/orders/{id}")
    public OrderAdminDetailDto getById(@PathVariable String id) {
        return orderService.getAdminById(id);
    }

    @GetMapping("/api/admin/orders/export")
    public ResponseEntity<byte[]> exportExcel() {
        byte[] file = orderService.exportAdminExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        ));
        headers.setContentDisposition(ContentDisposition
                .attachment()
                .filename("orders.xlsx")
                .build());

        return new ResponseEntity<>(file, headers, HttpStatus.OK);
    }
}