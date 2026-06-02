package com.example.indentory_management_system.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.indentory_management_system.Service.StockService;
import com.example.indentory_management_system.dto.StockRequestdto;
import com.example.indentory_management_system.dto.StockResponsedto;
import com.example.indentory_management_system.dto.StockTransferRequestdto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stock")
public class StockController {
    private final StockService stockService;

    @PostMapping("/addstock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<StockResponsedto> addStock(@Valid @RequestBody StockRequestdto dto) {
        return ResponseEntity.ok(stockService.addStock(dto));
    }

    @GetMapping("/getallstock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<List<StockResponsedto>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @PutMapping("/updatestock/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<StockResponsedto> updateStock(@PathVariable Long id, @Valid @RequestBody StockRequestdto dto) {
        return ResponseEntity.ok(stockService.updateStock(id, dto));
    }

    @DeleteMapping("/deletestock/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transferstock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<StockResponsedto>> transferStock(@Valid @RequestBody StockTransferRequestdto dto) {
        StockRequestdto serviceDto = new StockRequestdto();
        serviceDto.setProduct_id(dto.getProduct_id());
        serviceDto.setQuantityOnHand(dto.getQuantityOnHand());
        return ResponseEntity
                .ok(stockService.transferStock(dto.getFromWarehouseId(), dto.getToWarehouseId(), serviceDto));
    }

    @GetMapping("/getcurrentstock/{productId}/{warehouseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<StockResponsedto> getCurrentStock(@PathVariable Long productId,
            @PathVariable Long warehouseId) {
        return ResponseEntity.ok(stockService.getCurrentStock(productId, warehouseId));
    }
}
