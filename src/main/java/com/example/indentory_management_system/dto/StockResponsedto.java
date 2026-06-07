package com.example.indentory_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockResponsedto {

    private Long id;
    private String productName;
    private String warehouseName;
    private Integer quantityOnHand;

}
