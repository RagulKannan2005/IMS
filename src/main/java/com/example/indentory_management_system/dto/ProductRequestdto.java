package com.example.indentory_management_system.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestdto {
    @NotBlank(message = "Product SKU is required")
    private String sku;

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @Min(value = 0, message = "Cost price cannot be negative")
    private double costPrice;

    @Min(value = 0, message = "Selling price cannot be negative")
    private double sellingPrice;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    private int stockQuantity;

    @Min(value = 0, message = "Reorder level cannot be negative")
    private int reorderLevel;

    @Min(value = 0, message = "Reorder quantity cannot be negative")
    private int reorderQuantity;

    @NotBlank(message = "Active status is required")
    private String active_status;

    @NotBlank(message = "Category name is required")
    private String category;

    private Long supplierId;
}
