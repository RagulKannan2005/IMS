package com.example.indentory_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestdto {
    private String sku;
    private String name;
    private String description;
    private double costPrice;
    private double sellingPrice;
    private int stockQuantity;
    private int reorderLevel;
    private int reorderQuantity;
    private String active_status;
    private String category;
}
