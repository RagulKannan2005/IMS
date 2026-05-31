package com.example.indentory_management_system.Entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class warehouses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,name="name",length = 50)
    private String name;
    
    @Column(nullable = false,name="warehouse_code",length = 50,unique = true)
    private String warehouseCode;

    @Column(nullable = false,name = "capacity")
    private Integer capacity;

    @Column(nullable = false,name = "manager_name")
    private String managerName;

    @Column(nullable = false,name = "contact_number")
    private String contactNumber;

    @Column(nullable = false,name = "email")
    private String email;

    @Column(nullable = false,name = "is_active")
    private String isActive;

    @Column(nullable = false,name = "created_date")
    private LocalDate createdDate;

    @Column(nullable = false,name = "updated_date")
    private LocalDate updatedDate;
    
    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDate.now();
        this.updatedDate = LocalDate.now();
    }
    @PreUpdate
    public void preUpdate() {
        this.updatedDate = LocalDate.now();
    }
}   
