package com.example.indentory_management_system.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderItemResponsedto {
    private Long id;
    private Long purchaseOrderId;
    private String poNumber;
    private Long productId;
    private String productName;
    private int quantityOrdered;
    private int quantityReceived;
    private BigDecimal unitCost;
    private BigDecimal totalCost;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
