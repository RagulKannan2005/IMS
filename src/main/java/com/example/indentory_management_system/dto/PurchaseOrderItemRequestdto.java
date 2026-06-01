package com.example.indentory_management_system.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PurchaseOrderItemRequestdto {

    @NotNull(message = "Purchase Order ID is required")
    private Long purchaseOrderId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @Min(value = 1, message = "Quantity ordered must be greater than 0")
    private int quantityOrdered;

    @Min(value = 0, message = "Quantity received cannot be negative")
    private int quantityReceived;

    @NotNull(message = "Unit cost is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit cost must be greater than 0")
    private BigDecimal unitCost;
}
