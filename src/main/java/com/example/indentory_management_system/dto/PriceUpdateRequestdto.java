package com.example.indentory_management_system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PriceUpdateRequestdto {

    @NotNull(message = "Selling price is required")
    private Double sellingPrice;

    @NotNull(message = "Cost Price is required")
    private Double costPrice;
}
