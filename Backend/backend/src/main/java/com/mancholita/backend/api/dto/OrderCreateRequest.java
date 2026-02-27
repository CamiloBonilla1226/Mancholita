package com.mancholita.backend.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public class OrderCreateRequest {

    @NotBlank
    public String customerName;

    @NotBlank
    @Email
    public String email;

    @NotBlank
    public String phone;

    @NotBlank
    public String documentNumber;

    @NotBlank
    public String department;

    @NotBlank
    public String municipality;

    @NotBlank
    public String address;

    @NotEmpty
    @Valid
    public List<OrderItemRequest> items;

    public static class OrderItemRequest {
        @NotNull
        public Long productId;

        @NotNull
        @Min(1)
        public Integer quantity;
    }
}