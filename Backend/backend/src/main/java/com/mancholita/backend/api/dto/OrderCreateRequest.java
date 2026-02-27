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
@Pattern(regexp = "^[0-9]{7,15}$", message = "phone must contain only digits (7 to 15)")
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
@Size(max = 50, message = "items max is 50")
@Valid
public List<OrderItemRequest> items;

    public static class OrderItemRequest {
        @NotNull
        public Long productId;

        @NotNull
@Min(1)
@Max(value = 50, message = "quantity max is 50")
public Integer quantity;
    }
}