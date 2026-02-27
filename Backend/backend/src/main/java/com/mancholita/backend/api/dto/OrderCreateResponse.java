package com.mancholita.backend.api.dto;

import java.math.BigDecimal;

public class OrderCreateResponse {
    public String orderId;
    public BigDecimal total;
    public String whatsappUrl;

    public OrderCreateResponse(String orderId, BigDecimal total, String whatsappUrl) {
        this.orderId = orderId;
        this.total = total;
        this.whatsappUrl = whatsappUrl;
    }
}