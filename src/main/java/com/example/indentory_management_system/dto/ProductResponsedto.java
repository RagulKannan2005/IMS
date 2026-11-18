package com.example.indentory_management_system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("isActive")
    private boolean isActive;

    private Long supplierId;
    private String supplierName;
    private String categoryName;

    @JsonProperty("isActive")
    public boolean getIsActive() {
        return this.isActive;
    }

    @JsonProperty("active")
    public boolean getActive() {
        return this.isActive;
    }
}
