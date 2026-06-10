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
@RequestMapping("/api/v1/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseorderservice;

    @PostMapping("/addpurchaseorder")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public PurchaseOrderResponsedto addPurchaseOrder(@Valid @RequestBody PurchaseOrderRequestdto dto){
        return purchaseorderservice.addPurchaseOrder(dto);
    }

    @PostMapping("/updatepurchaseorder/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public PurchaseOrderResponsedto updatePurchaseOrder(@PathVariable Long id, @Valid @RequestBody PurchaseOrderRequestdto dto){
        return purchaseorderservice.updatePurchaseOrder(id, dto);
    }

    @PostMapping("/deletepurchaseorder/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void deletePurchaseOrder(@PathVariable Long id){
        purchaseorderservice.deletePurchaseOrder(id);
    }

    @PostMapping("/findbypurchaseordernumber")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public PurchaseOrderResponsedto findByPurchaseOrderNumber(@RequestParam String poNumber){
        return purchaseorderservice.findByPoNumber(poNumber);
    }

    @PostMapping("/findallpurchaseorders")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<PurchaseOrderResponsedto> findAllPurchaseOrders(){
        return purchaseorderservice.getAllPurchaseOrders();
    }

    @PostMapping("/findbysupplierid")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or (hasRole('SUPPLIER') and @securityService.isOwnSupplierId(authentication, #supplierId))")
    public List<PurchaseOrderResponsedto> findBySupplierId(@RequestParam Long supplierId){
        return purchaseorderservice.findBySupplierId(supplierId);
    }

    @PostMapping("/findbystatus")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<PurchaseOrderResponsedto> findByStatus(@RequestParam String status){
        return purchaseorderservice.findByStatus(status);
    }

    @PostMapping("/findbycreatedby")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<PurchaseOrderResponsedto> findByCreatedBy(@RequestParam Long userId){
        return purchaseorderservice.findByCreatedBy(userId);
    }

    @PostMapping("/findbyorderdatetrange")
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

    @PutMapping("/{id}/accept")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or (hasRole('SUPPLIER') and @securityService.isSupplierForOrder(authentication, #id))")
    public PurchaseOrderResponsedto acceptOrder(@PathVariable Long id) {
        return purchaseorderservice.acceptOrder(id);
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or (hasRole('SUPPLIER') and @securityService.isSupplierForOrder(authentication, #id))")
    public PurchaseOrderResponsedto rejectOrder(@PathVariable Long id) {
        return purchaseorderservice.rejectOrder(id);
    }

    @PutMapping("/{id}/ship")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or (hasRole('SUPPLIER') and @securityService.isSupplierForOrder(authentication, #id))")
    public PurchaseOrderResponsedto shipOrder(@PathVariable Long id) {
        return purchaseorderservice.shipOrder(id);
    }
}
