package com.example.indentory_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponsedto {
    private Long id;
    private String sku;
    private String name;
    private String description;
    private double costPrice;
    private double sellingPrice;
    private int stockQuantity;
    private int reorderLevel;
    private int reorderQuantity;
    private boolean isActive;
}
