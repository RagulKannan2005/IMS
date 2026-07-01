package com.example.indentory_management_system.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierProductRequestdto {
    @NotBlank(message = "Product SKU is required")
    private String sku;

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @Min(value = 0, message = "Cost price cannot be negative")
    private double costPrice;

    @Min(value = 0, message = "Available quantity cannot be negative")
    private int availableQuantity;

    @NotBlank(message = "Active status is required")
    private String active_status;

    @NotBlank(message = "Category name is required")
    private String category;
}
