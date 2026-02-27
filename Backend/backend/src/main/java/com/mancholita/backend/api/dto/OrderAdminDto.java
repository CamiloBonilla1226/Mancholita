package com.mancholita.backend.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderAdminDto {
    public String id;
    public LocalDateTime createdAt;
    public String customerName;
    public String email;
    public String phone;
    public String documentNumber;
    public String department;
    public String municipality;
    public String address;
    public BigDecimal total;

    public OrderAdminDto(String id, LocalDateTime createdAt, String customerName, String email, String phone,
                         String documentNumber, String department, String municipality, String address, BigDecimal total) {
        this.id = id;
        this.createdAt = createdAt;
        this.customerName = customerName;
        this.email = email;
        this.phone = phone;
        this.documentNumber = documentNumber;
        this.department = department;
        this.municipality = municipality;
        this.address = address;
        this.total = total;
    }
}