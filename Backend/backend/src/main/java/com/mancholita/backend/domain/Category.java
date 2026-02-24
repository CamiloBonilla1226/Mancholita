package com.mancholita.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "categories")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    protected Category() {}

    // Root category constructor (Hombre/Mujer)
    public Category(String name, boolean active) {
        this(name, active, null);
    }

    // Child category constructor (Jeans/Camisas/Blusas)
    public Category(String name, boolean active, Category parent) {
        this.name = name;
        this.active = active;
        this.parent = parent;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public boolean isActive() { return active; }
    public Category getParent() { return parent; }

    public void setActive(boolean active) { this.active = active; }
    public void setName(String name) { this.name = name; }
    public void setParent(Category parent) { this.parent = parent; }
}