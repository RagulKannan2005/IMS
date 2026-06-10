package com.example.indentory_management_system.Service;

import java.util.List;

import com.example.indentory_management_system.dto.SupplierProductDto;

public interface SupplierProductService {
    List<SupplierProductDto> getSupplierProductsBySupplierId(Long supplierId);
}
