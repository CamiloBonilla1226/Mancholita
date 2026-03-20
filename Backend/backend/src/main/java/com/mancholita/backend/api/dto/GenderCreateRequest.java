package com.mancholita.backend.api.dto;

import jakarta.validation.constraints.NotBlank;

public class GenderCreateRequest {

    @NotBlank
    public String name;

    public Boolean active;
}
