package com.example.indentory_management_system.Service;

import java.time.LocalDateTime;
import java.util.List;
import com.example.indentory_management_system.dto.StockMovementRequestDto;
import com.example.indentory_management_system.dto.StockMovementResponseDto;

public interface StockMovementService {

    StockMovementResponseDto createMovement(StockMovementRequestDto dto);

    StockMovementResponseDto getMovementById(Long id);

    List<StockMovementResponseDto> getAllMovements();

    List<StockMovementResponseDto> getMovementsByProduct(Long productId);

    List<StockMovementResponseDto> getMovementsByWarehouse(Long warehouseId);

    List<StockMovementResponseDto> getMovementsByType(String type);

    List<StockMovementResponseDto> getMovementsByDateRange(LocalDateTime start, LocalDateTime end);

    StockMovementResponseDto updateMovement(Long id, StockMovementRequestDto dto);

    void deleteMovement(Long id);
}
