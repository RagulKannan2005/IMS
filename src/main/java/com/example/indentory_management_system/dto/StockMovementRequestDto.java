package com.example.indentory_management_system.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovementRequestDto {

    @NotNull(message = "Product ID is required")
    private Long product_id;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouse_id;

    @NotBlank(message = "Movement type is required (IN, OUT, ADJUSTMENT, TRANSFER)")
    private String movement_type;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private String reference_no;

    private Long performed_by;

    private String remarks;

    // For Transfer specific operations
    private Long toWarehouseId;
}
