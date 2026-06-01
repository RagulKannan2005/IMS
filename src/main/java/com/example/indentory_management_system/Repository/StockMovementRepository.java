package com.example.indentory_management_system.Repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.indentory_management_system.Entity.StockMovement;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    List<StockMovement> findByProductsId(Long productId);

    List<StockMovement> findByWarehousesId(Long warehouseId);

    List<StockMovement> findByMovementType(String movementType);

    List<StockMovement> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
