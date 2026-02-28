package com.mancholita.backend.api;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mancholita.backend.api.dto.OrderAdminDetailDto;
import com.mancholita.backend.api.dto.OrderAdminListDto;
import com.mancholita.backend.application.OrderService;

@RestController
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ✅ B) Listado admin: id, nombre, celular, total
    @GetMapping("/api/admin/orders")
    public Page<OrderAdminListDto> list(
            @RequestParam(required = false) String q,
            Pageable pageable
    ) {
        return orderService.listAdmin(q, pageable);
    }

    // ✅ A) Excel único (todo)
    @GetMapping("/api/admin/orders/export/all")
    public ResponseEntity<byte[]> exportAll() {
        byte[] file = orderService.exportExcelAll();
        return excelResponse(file, "orders_all.xlsx");
    }

    // ✅ A) Excel por día
    // Ej: /api/admin/orders/export/daily?date=2026-02-27
    @GetMapping("/api/admin/orders/export/daily")
    public ResponseEntity<byte[]> exportDaily(@RequestParam LocalDate date) {
        byte[] file = orderService.exportExcelDaily(date);
        return excelResponse(file, "orders_" + date + ".xlsx");
    }

    private ResponseEntity<byte[]> excelResponse(byte[] file, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        ));
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());
        return new ResponseEntity<>(file, headers, HttpStatus.OK);
    }

    @GetMapping("/api/admin/orders/{id}")
public OrderAdminDetailDto detail(@PathVariable String id) {
    return orderService.getAdminDetail(id);
}
}