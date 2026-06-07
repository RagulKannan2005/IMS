package com.example.indentory_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockTransferRequestdto {

    private Long productId;
    private Long fromWarehouseId;
    private Long toWarehouseId;
    private Integer quantityOnHand;

}
