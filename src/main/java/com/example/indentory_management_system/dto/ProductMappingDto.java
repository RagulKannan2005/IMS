package com.example.indentory_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductMappingDto {
    private Long purchaseOrderItemId;
    private String action; // "CREATE" or "LINK"
    private Long internalProductId; // Required if action is "LINK"
}
