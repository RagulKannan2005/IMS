package com.example.indentory_management_system.dto;

import java.util.List;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffDashboardDto {
    private long stockInToday;
    private long stockOutToday;
    private List<StockMovementResponseDto> stockMovementsToday;
    private List<ProductResponsedto> lowStockProducts;
    private Long totalInternalProducts;
    private List<ProductSummaryDto> recentInternalProducts;
}
