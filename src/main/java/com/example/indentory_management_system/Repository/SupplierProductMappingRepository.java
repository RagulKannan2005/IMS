package com.example.indentory_management_system.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.indentory_management_system.Entity.SupplierProductMapping;

@Repository
public interface SupplierProductMappingRepository extends JpaRepository<SupplierProductMapping, Long> {
    Optional<SupplierProductMapping> findBySupplierProductId(Long supplierProductId);
}
