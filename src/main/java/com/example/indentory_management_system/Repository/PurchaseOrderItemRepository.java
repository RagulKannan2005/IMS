package com.example.indentory_management_system.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.indentory_management_system.Entity.PurchaseOrderItem;

@Repository
public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Long> {

    @Query("SELECT p FROM PurchaseOrderItem p WHERE p.purchaseOrder.id = :purchaseOrderId")
    List<PurchaseOrderItem> findByPurchaseOrderId(@Param("purchaseOrderId") Long purchaseOrderId);

    @Query("SELECT p FROM PurchaseOrderItem p WHERE p.product.id = :productId")
    List<PurchaseOrderItem> findByProductId(@Param("productId") Long productId);
}
