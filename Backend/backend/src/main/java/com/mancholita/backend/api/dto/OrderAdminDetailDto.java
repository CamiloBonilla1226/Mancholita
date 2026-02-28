package com.mancholita.backend.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderAdminDetailDto {
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
    public List<Item> items;

    public static class Item {
        public Long productId;
        public String productName;
        public BigDecimal unitPrice;
        public int quantity;
        public BigDecimal lineTotal;

        public Item(Long productId, String productName, BigDecimal unitPrice, int quantity, BigDecimal lineTotal) {
            this.productId = productId;
            this.productName = productName;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
            this.lineTotal = lineTotal;
        }
    }

    public OrderAdminDetailDto(String id, LocalDateTime createdAt, String customerName, String email, String phone,
                               String documentNumber, String department, String municipality, String address,
                               BigDecimal total, List<Item> items) {
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
        this.items = items;
    }
}