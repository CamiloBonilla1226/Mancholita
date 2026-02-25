package com.mancholita.backend.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class ProductCreateRequest {

    @NotBlank
    public String name;

    public String description;

    @NotNull
    @Positive
    public BigDecimal price;

    public Boolean active;

    @NotBlank
    public String imageUrl;

    @NotNull
    public Long categoryId;
}