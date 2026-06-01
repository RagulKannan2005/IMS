package com.example.indentory_management_system.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public PurchaseOrderResponsedto addPurchaseOrder(@Valid @RequestBody PurchaseOrderRequestdto dto){
        return purchaseorderservice.addPurchaseOrder(dto);
    }

    @PostMapping("/updatepurchaseorder/{id}")
    public PurchaseOrderResponsedto updatePurchaseOrder(@PathVariable Long id, @Valid @RequestBody PurchaseOrderRequestdto dto){
        return purchaseorderservice.updatePurchaseOrder(id, dto);
    }

    @PostMapping("/deletepurchaseorder/{id}")
    public void deletePurchaseOrder(@PathVariable Long id){
        purchaseorderservice.deletePurchaseOrder(id);
    }

    @PostMapping("/findbypurchaseordernumber")
    public PurchaseOrderResponsedto findByPurchaseOrderNumber(@RequestParam String poNumber){
        return purchaseorderservice.findByPoNumber(poNumber);
    }

    @PostMapping("/findallpurchaseorders")
    public List<PurchaseOrderResponsedto> findAllPurchaseOrders(){
        return purchaseorderservice.getAllPurchaseOrders();
    }

    @PostMapping("/findbysupplierid")
    public List<PurchaseOrderResponsedto> findBySupplierId(@RequestParam Long supplierId){
        return purchaseorderservice.findBySupplierId(supplierId);
    }

    @PostMapping("/findbystatus")
    public List<PurchaseOrderResponsedto> findByStatus(@RequestParam String status){
        return purchaseorderservice.findByStatus(status);
    }

    @PostMapping("/findbycreatedby")
    public List<PurchaseOrderResponsedto> findByCreatedBy(@RequestParam Long userId){
        return purchaseorderservice.findByCreatedBy(userId);
    }

    @PostMapping("/findbyorderdatetrange")
    public List<PurchaseOrderResponsedto> findByOrderDateRange(@RequestParam LocalDate from, @RequestParam LocalDate to){
        return purchaseorderservice.findByOrderDateRange(from, to);
    }

    @PostMapping("/receivepurchaseorder")
    public PurchaseOrderResponsedto receivePurchaseOrder(@RequestParam Long id){
        return purchaseorderservice.receivePurchaseOrder(id);
    }
}
