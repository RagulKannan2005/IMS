package com.example.indentory_management_system.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierProductDto {
    private Long id;
    private Long supplierId;
    private Long productId;
    private String productName;
    private String productSku;
    private int availableQuantity;
    private BigDecimal unitPrice;
}
