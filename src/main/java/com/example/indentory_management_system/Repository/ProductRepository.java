package com.example.indentory_management_system.Repository;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.indentory_management_system.Entity.Products;

public interface ProductRepository extends JpaRepository<Products, Long> {

    @Query("SELECT p FROM Products p WHERE p.sku = ?1")
    Optional<Products> findBySku(String sku);

    @Query("SELECT p FROM Products p WHERE p.name = ?1")
    Optional<Products> findByName(String name);

    @Query("SELECT p FROM Products p WHERE p.categories.name = ?1")
    List<Products> findByCategoryName(String name);
}
