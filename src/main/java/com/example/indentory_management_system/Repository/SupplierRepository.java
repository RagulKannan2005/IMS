package com.example.indentory_management_system.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.indentory_management_system.Entity.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @Query("SELECT s FROM Supplier s JOIN s.supplierProducts p WHERE p.name LIKE %:productName%")
    List<Supplier> findSuppliersByProductName(String productName);

    @Query("SELECT s FROM Supplier s WHERE s.supplierName LIKE %:keyword% OR s.contactPerson LIKE %:keyword%")
    List<Supplier> searchSuppliers(String keyword);

    @Query("SELECT s FROM Supplier s WHERE s.status = :status")
    List<Supplier> findSuppliersByStatus(boolean status);

    @Query("SELECT s FROM Supplier s WHERE s.supplierName LIKE %:keyword%")
    List<Supplier> findSuppliersByName(String keyword);

    @Query("SELECT s FROM Supplier s WHERE s.user.id = :userId")
    List<Supplier> findByUserId(Long userId);

    @Query("SELECT s FROM Supplier s JOIN s.supplierProducts p WHERE p.name LIKE %:productName% AND s.user.id = :userId")
    List<Supplier> findSuppliersByProductNameAndUserId(String productName, Long userId);

    @Query("SELECT s FROM Supplier s WHERE (s.supplierName LIKE %:keyword% OR s.contactPerson LIKE %:keyword%) AND s.user.id = :userId")
    List<Supplier> searchSuppliersByUserId(String keyword, Long userId);

    @Query("SELECT s FROM Supplier s WHERE s.status = :status AND s.user.id = :userId")
    List<Supplier> findSuppliersByStatusAndUserId(boolean status, Long userId);

    @Query("SELECT s FROM Supplier s WHERE s.supplierName LIKE %:keyword% AND s.user.id = :userId")
    List<Supplier> findSuppliersByNameAndUserId(String keyword, Long userId);
}
