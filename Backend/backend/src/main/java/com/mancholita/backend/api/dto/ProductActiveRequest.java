package com.mancholita.backend.api.dto;

import jakarta.validation.constraints.NotNull;

public class ProductActiveRequest {

    @NotNull
    public Boolean active;
}