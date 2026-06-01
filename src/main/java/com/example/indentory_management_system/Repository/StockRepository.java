package com.example.indentory_management_system.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.indentory_management_system.Entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("SELECT s FROM Stock s WHERE s.Products.id = :productId AND s.warehouses.id = :warehouseId")
    Optional<Stock> findByProductsIdAndWarehousesId(@Param("productId") Long productId, @Param("warehouseId") Long warehouseId);

}
