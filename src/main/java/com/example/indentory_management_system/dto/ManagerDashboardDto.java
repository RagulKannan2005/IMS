package com.example.indentory_management_system.dto;

import java.util.List;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerDashboardDto {
    private long pendingPurchaseOrders;
    private long incomingShipments;
    private long activeSuppliers;
    private List<ProductResponsedto> lowStockProducts;
    private Long totalInternalProducts;
    private List<ProductSummaryDto> recentInternalProducts;
    private List<PurchaseOrderResponsedto> recentOrders;
}
