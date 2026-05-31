package com.example.indentory_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockAdjustmentRequest {

    @NotNull(message = "Quantity change is required")
    private Integer quantityChange;

    @NotBlank(message = "Reason for adjustment is required")
    private String reason;
}
