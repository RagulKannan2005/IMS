package com.example.indentory_management_system.Entity;

import lombok.AllArgsConstructor;

import java.time.*;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String active_status;

    @Column(nullable = false)
    private LocalDate created_at;

    @Column(nullable = false)
    private LocalDate updated_at;

    @PrePersist
    public void prePersist() {
        this.created_at = LocalDate.now();
        this.updated_at = LocalDate.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updated_at = LocalDate.now();
    }
    @OneToMany(mappedBy = "categories",cascade = CascadeType.ALL)
    private List<Products> products;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;
}
