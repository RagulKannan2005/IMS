package com.example.indentory_management_system.Service;

import java.time.LocalDate;
import java.util.List;
import com.example.indentory_management_system.dto.PurchaseOrderRequestdto;
import com.example.indentory_management_system.dto.PurchaseOrderResponsedto;

public interface PurchaseOrderService {

    PurchaseOrderResponsedto addPurchaseOrder(PurchaseOrderRequestdto dto);

    PurchaseOrderResponsedto updatePurchaseOrder(Long id, PurchaseOrderRequestdto dto);

    void deletePurchaseOrder(Long id);

    PurchaseOrderResponsedto findByPoNumber(String poNumber);

    List<PurchaseOrderResponsedto> getAllPurchaseOrders();

    List<PurchaseOrderResponsedto> findBySupplierId(Long supplierId);

    List<PurchaseOrderResponsedto> findByStatus(String status);

    List<PurchaseOrderResponsedto> findByCreatedBy(Long userId);

    List<PurchaseOrderResponsedto> findByOrderDateRange(LocalDate from, LocalDate to);

    PurchaseOrderResponsedto receivePurchaseOrder(Long id);

    PurchaseOrderResponsedto acceptOrder(Long id);

    PurchaseOrderResponsedto rejectOrder(Long id);

    PurchaseOrderResponsedto shipOrder(Long id);

    PurchaseOrderResponsedto updateStatus(Long id, String status);
}
