package com.example.indentory_management_system.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovementResponseDto {

    private Long id;
    private Long productId;
    private String productName;
    private Long warehouseId;
    private String warehouseName;
    private String movementType;
    private Integer quantity;
    private String referenceNo;
    private Long performedById;
    private String performedByUsername;
    private String remarks;
    private LocalDateTime createdAt;

}
