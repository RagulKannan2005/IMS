package com.example.indentory_management_system.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDashboardDto {
    private long myProducts;
    private long myOrders;
    private long shippedOrders;
    private long pendingOrders;
}
