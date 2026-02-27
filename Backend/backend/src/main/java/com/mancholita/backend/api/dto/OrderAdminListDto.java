package com.mancholita.backend.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderAdminListDto {
    public String id;
    public LocalDateTime createdAt;
    public String customerName;
    public String phone;
    public BigDecimal total;

    public OrderAdminListDto(String id, LocalDateTime createdAt, String customerName, String phone, BigDecimal total) {
        this.id = id;
        this.createdAt = createdAt;
        this.customerName = customerName;
        this.phone = phone;
        this.total = total;
    }
}