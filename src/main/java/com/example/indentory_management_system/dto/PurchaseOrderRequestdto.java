package com.example.indentory_management_system.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import jakarta.validation.Valid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PurchaseOrderRequestdto {
    @NotBlank(message = "PO Number is required")
    private String poNumber;

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    @NotNull(message = "Created By is required")
    private Long createdBy;

    @NotNull(message = "Total Amount is required")
    private BigDecimal totalAmount;

    @NotBlank(message = "Status is required")
    private String status;

    private LocalDate orderedAt;

    @NotNull(message = "Expected Delivery Date is required")
    private LocalDate expectedDeliveryDate;

    private String remarks;

    @Valid
    private List<PurchaseOrderItemRequestdto> items;

}
