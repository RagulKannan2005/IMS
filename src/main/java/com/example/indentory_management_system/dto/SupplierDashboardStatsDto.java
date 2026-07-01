package com.example.indentory_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierDashboardStatsDto {
    private long pendingOrders;
    private long inTransitOrders;
    private long totalProducts;
    private double monthlyRevenue;
}
