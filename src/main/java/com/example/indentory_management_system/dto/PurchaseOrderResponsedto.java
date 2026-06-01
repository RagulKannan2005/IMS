package com.example.indentory_management_system.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderResponsedto {
    private Long id;
    private String poNumber;
    private String supplierName;
    private String createdBy;
    private BigDecimal totalAmount;
    private String status;
    private LocalDate orderedAt;
    private LocalDate expectedDeliveryDate;
    private String remarks;
}
