package com.example.indentory_management_system.dto;

import lombok.Data;

@Data
public class StockRequestdto {

    private Long product_id;

    private Long warehouse_id;

    private Integer quantityOnHand;
}


