package com.example.indentory_management_system.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.indentory_management_system.Service.PurchaseOrderService;
import com.example.indentory_management_system.dto.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/purrchaseorder")
@RequiredArgsConstructor
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseorderservice;

    @PostMapping("/addpurchaseorder")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public PurchaseOrderResponsedto addPurchaseOrder(@Valid @RequestBody PurchaseOrderRequestdto dto){
        return purchaseorderservice.addPurchaseOrder(dto);
    }

    @PutMapping("/updatepurchaseorder/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public PurchaseOrderResponsedto updatePurchaseOrder(@PathVariable Long id, @Valid @RequestBody PurchaseOrderRequestdto dto){
        return purchaseorderservice.updatePurchaseOrder(id, dto);
    }

    @DeleteMapping("/deletepurchaseorder/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void deletePurchaseOrder(@PathVariable Long id){
        purchaseorderservice.deletePurchaseOrder(id);
    }

    @GetMapping("/findbypurchaseordernumber")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public PurchaseOrderResponsedto findByPurchaseOrderNumber(@RequestParam String poNumber){
        return purchaseorderservice.findByPoNumber(poNumber);
    }

    @GetMapping("/findallpurchaseorders")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<PurchaseOrderResponsedto> findAllPurchaseOrders(){
        return purchaseorderservice.getAllPurchaseOrders();
    }

    @GetMapping("/findbysupplierid")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or (hasRole('SUPPLIER') and @securityService.isOwnSupplierId(authentication, #supplierId))")
    public List<PurchaseOrderResponsedto> findBySupplierId(@RequestParam Long supplierId){
        return purchaseorderservice.findBySupplierId(supplierId);
    }

    @GetMapping("/findbystatus")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<PurchaseOrderResponsedto> findByStatus(@RequestParam String status){
        return purchaseorderservice.findByStatus(status);
    }

    @GetMapping("/findbycreatedby")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<PurchaseOrderResponsedto> findByCreatedBy(@RequestParam Long userId){
        return purchaseorderservice.findByCreatedBy(userId);
    }

    @GetMapping("/findbyorderdatetrange")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<PurchaseOrderResponsedto> findByOrderDateRange(@RequestParam LocalDate from, @RequestParam LocalDate to){
        return purchaseorderservice.findByOrderDateRange(from, to);
    }

    @PostMapping("/receivepurchaseorder")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public PurchaseOrderResponsedto receivePurchaseOrder(@RequestParam Long id){
        return purchaseorderservice.receivePurchaseOrder(id);
    }

    @PostMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or (hasRole('SUPPLIER') and @securityService.isSupplierForOrder(authentication, #id))")
    public PurchaseOrderResponsedto updateStatus(@PathVariable Long id, @RequestParam String status) {
        return purchaseorderservice.updateStatus(id, status);
    }
}
