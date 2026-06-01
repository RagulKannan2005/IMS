package com.example.indentory_management_system.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.indentory_management_system.Service.PurchaseOrderItemService;
import com.example.indentory_management_system.dto.PurchaseOrderItemRequestdto;
import com.example.indentory_management_system.dto.PurchaseOrderItemResponsedto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/purchaseorderitems")
@RequiredArgsConstructor
public class PurchaseOrderItemController {

    private final PurchaseOrderItemService purchaseOrderItemService;

    @PostMapping
    public ResponseEntity<PurchaseOrderItemResponsedto> addPurchaseOrderItem(
            @Valid @RequestBody PurchaseOrderItemRequestdto dto) {
        PurchaseOrderItemResponsedto response = purchaseOrderItemService.addPurchaseOrderItem(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrderItemResponsedto> updatePurchaseOrderItem(
            @PathVariable Long id,
            @Valid @RequestBody PurchaseOrderItemRequestdto dto) {
        PurchaseOrderItemResponsedto response = purchaseOrderItemService.updatePurchaseOrderItem(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseOrderItem(@PathVariable Long id) {
        purchaseOrderItemService.deletePurchaseOrderItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderItemResponsedto> getPurchaseOrderItemById(@PathVariable Long id) {
        PurchaseOrderItemResponsedto response = purchaseOrderItemService.getPurchaseOrderItemById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrderItemResponsedto>> getAllPurchaseOrderItems() {
        List<PurchaseOrderItemResponsedto> response = purchaseOrderItemService.getAllPurchaseOrderItems();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/purchaseorder/{purchaseOrderId}")
    public ResponseEntity<List<PurchaseOrderItemResponsedto>> getItemsByPurchaseOrder(
            @PathVariable Long purchaseOrderId) {
        List<PurchaseOrderItemResponsedto> response = purchaseOrderItemService.getItemsByPurchaseOrder(purchaseOrderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<PurchaseOrderItemResponsedto>> getItemsByProduct(
            @PathVariable Long productId) {
        List<PurchaseOrderItemResponsedto> response = purchaseOrderItemService.getItemsByProduct(productId);
        return ResponseEntity.ok(response);
    }
}
