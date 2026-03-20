package com.mancholita.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "genders",
    uniqueConstraints = @UniqueConstraint(name = "uk_genders_name", columnNames = "name")
)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Gender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(nullable = false)
    private boolean active = true;

    protected Gender() {}

    public Gender(String name, boolean active) {
        this.name = name;
        this.active = active;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public boolean isActive() { return active; }

    public void setName(String name) { this.name = name; }
    public void setActive(boolean active) { this.active = active; }
}
