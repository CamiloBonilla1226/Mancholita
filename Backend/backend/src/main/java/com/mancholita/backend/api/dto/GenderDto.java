package com.mancholita.backend.api.dto;

public class GenderDto {

    public Long id;
    public String name;
    public boolean active;

    public GenderDto(Long id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }
}
