package com.example.indentory_management_system.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.indentory_management_system.Entity.PurchaseOrder;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    Optional<PurchaseOrder> findByPoNumber(String poNumber);

    @Query("SELECT p FROM PurchaseOrder p WHERE p.supplier.id = :supplierId")
    List<PurchaseOrder> findBySupplierId(@Param("supplierId") Long supplierId);

    @Query("SELECT p FROM PurchaseOrder p WHERE p.status = :status")
    List<PurchaseOrder> findByStatus(@Param("status") String status);

    @Query("SELECT p FROM PurchaseOrder p WHERE p.user.id = :userId")
    List<PurchaseOrder> findByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM PurchaseOrder p WHERE p.orderedAt BETWEEN :start AND :end")
    List<PurchaseOrder> findByOrderedAtBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
