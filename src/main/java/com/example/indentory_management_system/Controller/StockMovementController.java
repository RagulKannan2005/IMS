package com.example.indentory_management_system.Controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.indentory_management_system.Service.StockMovementService;
import com.example.indentory_management_system.dto.StockMovementRequestDto;
import com.example.indentory_management_system.dto.StockMovementResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stock-movements")
public class StockMovementController {

    private final StockMovementService stockMovementService;

    @PostMapping
    public ResponseEntity<StockMovementResponseDto> createMovement(@Valid @RequestBody StockMovementRequestDto dto) {
        return ResponseEntity.ok(stockMovementService.createMovement(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockMovementResponseDto> getMovementById(@PathVariable Long id) {
        return ResponseEntity.ok(stockMovementService.getMovementById(id));
    }

    @GetMapping
    public ResponseEntity<List<StockMovementResponseDto>> getAllMovements() {
        return ResponseEntity.ok(stockMovementService.getAllMovements());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<StockMovementResponseDto>> getMovementsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(stockMovementService.getMovementsByProduct(productId));
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<StockMovementResponseDto>> getMovementsByWarehouse(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(stockMovementService.getMovementsByWarehouse(warehouseId));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<StockMovementResponseDto>> getMovementsByType(@PathVariable String type) {
        return ResponseEntity.ok(stockMovementService.getMovementsByType(type));
    }

    @GetMapping("/search-date")
    public ResponseEntity<List<StockMovementResponseDto>> getMovementsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(stockMovementService.getMovementsByDateRange(start, end));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockMovementResponseDto> updateMovement(
            @PathVariable Long id,
            @Valid @RequestBody StockMovementRequestDto dto) {
        return ResponseEntity.ok(stockMovementService.updateMovement(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovement(@PathVariable Long id) {
        stockMovementService.deleteMovement(id);
        return ResponseEntity.noContent().build();
    }
}
