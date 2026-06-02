package com.example.indentory_management_system.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="supplier")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String supplierName;

    @Column(nullable=false)
    private String contactPerson;

    @Column(nullable = false,unique=true)
    private String supplier_email;

    @Column(nullable =false,unique = true)
    private String supplierPhone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private boolean status;

    @Column(nullable=false)
    private LocalDate createDate;

    @Column(nullable=false)
    private LocalDate updateDate;
    
    @PrePersist
    public void prePersist() {
        this.createDate = LocalDate.now();
        this.updateDate = LocalDate.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updateDate = LocalDate.now();
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
    private Users user;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Products> products;
}
