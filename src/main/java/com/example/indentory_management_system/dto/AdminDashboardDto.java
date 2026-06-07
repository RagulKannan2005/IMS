package com.example.indentory_management_system.dto;

import java.util.List;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardDto {
    private long totalUsers;
    private long totalProducts;
    private long totalSuppliers;
    private long totalWarehouses;
    private long totalPurchaseOrders;
    private List<ProductResponsedto> lowStockProducts;
    private List<AuditLogResponseDto> auditLogs;
    private List<SystemHealthDto> platformStatus;
}
