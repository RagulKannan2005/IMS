package com.example.indentory_management_system.Entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone_number;

    @Column(nullable = false)
    private String role;

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
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Categories> categories;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Products> products;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = true)
    private Supplier supplier;

}
