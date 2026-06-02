package com.example.indentory_management_system.Entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,name = "sku",unique = true,length = 50)
    private String sku;

    @Column(nullable = false,name = "name",length = 50)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",nullable = false)
    private Categories categories;
    
    @Column(name="cost price",nullable = false)
    private double costPrice;

    @Column(name="selling price",nullable = false)
    private double sellingPrice;

    @Column(name="stock quantity",nullable = false)
    private int stockQuantity;

    @Column(name="reorder level",nullable = false)
    private int reorderLevel;

    @Column(name="reorder quantity",nullable = false)
    private int reorderQuantity;

    @Column(name="created_date",nullable = false,updatable = false)
    private LocalDate createdDate;

    @Column(name="updated_date",nullable = false)
    private LocalDate updatedDate;

    @Column(name="is_active",nullable = false)
    private boolean isActive;

    @PrePersist
    public void prePersist(){
        createdDate = LocalDate.now();
        updatedDate = LocalDate.now();
        isActive = true;
    }

    @PreUpdate
    public void preUpdate(){
        updatedDate = LocalDate.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = true)
    private Supplier supplier;
}
