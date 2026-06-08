package com.example.indentory_management_system.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.indentory_management_system.Entity.warehouses;

public interface WarehouseRepository extends JpaRepository<warehouses, Long> {
    
    @Query("SELECT w FROM warehouses w WHERE w.managerName = :managerName")
    List<warehouses> findbymanagername(String managerName);

    @Query("SELECT w FROM warehouses w WHERE w.warehouseCode = :warehouseCode")
    Optional<warehouses> findBywarehouseCode(String warehouseCode);

    @Query("SELECT w FROM warehouses w WHERE w.isActive = :status")
    List<warehouses> findbyisActive(String status);

    @Query("SELECT w FROM warehouses w WHERE w.name = :name")
    List<warehouses> findbywarehousename(String name);

    List<warehouses> findByUserId(Long userId);
}
