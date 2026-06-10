package com.example.indentory_management_system.Repository;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;
import com.example.indentory_management_system.Entity.Products;

public interface ProductRepository extends JpaRepository<Products, Long> {

    @EntityGraph(attributePaths = {"categories", "user", "supplier"})
    List<Products> findAll();

    @EntityGraph(attributePaths = {"categories", "user", "supplier"})
    @Query("SELECT p FROM Products p WHERE p.sku = ?1")
    Optional<Products> findBySku(String sku);

    @EntityGraph(attributePaths = {"categories", "user", "supplier"})
    @Query("SELECT p FROM Products p WHERE p.name = ?1")
    Optional<Products> findByName(String name);

    @EntityGraph(attributePaths = {"categories", "user", "supplier"})
    @Query("SELECT p FROM Products p WHERE p.categories.name = ?1")
    List<Products> findByCategoryName(String name);

    @EntityGraph(attributePaths = {"categories", "user", "supplier"})
    List<Products> findBySupplierId(Long supplierId);

    @EntityGraph(attributePaths = {"categories", "user", "supplier"})
    List<Products> findTop5BySupplierIsNullOrderByCreatedDateDesc();

    @Query("SELECT p FROM Products p WHERE p.stockQuantity <= p.reorderLevel")
    List<Products> findLowStockProducts();

    long countByIsActive(boolean isActive);

    long countBySupplierId(Long supplierId);
}
