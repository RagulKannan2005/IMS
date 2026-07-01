package com.example.indentory_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierProductResponsedto {
    private Long id;
    private String sku;
    private String name;
    private String description;
    private double costPrice;
    private int availableQuantity;
    private boolean isActive;
    private String category;
    private Long supplierId;
    private String supplierName;
}
