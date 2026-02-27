package com.mancholita.backend.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(length = 20)
    private String id; // ddMMyyyy + 4 alfanuméricos

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 120)
    private String customerName;

    @Column(nullable = false, length = 160)
    private String email;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(nullable = false, length = 30)
    private String documentNumber;

    @Column(nullable = false, length = 80)
    private String department;

    @Column(nullable = false, length = 80)
    private String municipality;

    @Column(nullable = false, length = 180)
    private String address;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    protected Order() { }

    public Order(String id,
                 LocalDateTime createdAt,
                 String customerName,
                 String email,
                 String phone,
                 String documentNumber,
                 String department,
                 String municipality,
                 String address,
                 BigDecimal total) {
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
    }

    public void addItem(OrderItem item) {
        item.attachTo(this);
        this.items.add(item);
    }

    public String getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getCustomerName() { return customerName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getDocumentNumber() { return documentNumber; }
    public String getDepartment() { return department; }
    public String getMunicipality() { return municipality; }
    public String getAddress() { return address; }
    public BigDecimal getTotal() { return total; }
    public List<OrderItem> getItems() { return items; }
}