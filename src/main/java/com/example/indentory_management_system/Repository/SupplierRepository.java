package com.example.indentory_management_system.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.indentory_management_system.Entity.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @Query("SELECT s FROM Supplier s JOIN s.products p WHERE p.name LIKE %:productName%")
    List<Supplier> findSuppliersByProductName(String productName);

    @Query("SELECT s FROM Supplier s WHERE s.supplierName LIKE %:keyword% OR s.contactPerson LIKE %:keyword%")
    List<Supplier> searchSuppliers(String keyword);

    @Query("SELECT s FROM Supplier s WHERE s.status = :status")
    List<Supplier> findSuppliersByStatus(boolean status);

    @Query("SELECT s FROM Supplier s WHERE s.supplierName LIKE %:keyword%")
    List<Supplier> findSuppliersByName(String keyword);

    long countByStatus(boolean status);

    @Query("SELECT DISTINCT s FROM Supplier s JOIN s.products p JOIN p.categories c WHERE c.name = :categoryName")
    List<Supplier> findByCategoryName(String categoryName);
}
