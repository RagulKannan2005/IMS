package com.example.indentory_management_system.Service;

import java.util.List;

import com.example.indentory_management_system.dto.PurchaseOrderItemRequestdto;
import com.example.indentory_management_system.dto.PurchaseOrderItemResponsedto;

public interface PurchaseOrderItemService {

    PurchaseOrderItemResponsedto addPurchaseOrderItem(PurchaseOrderItemRequestdto dto);

    PurchaseOrderItemResponsedto updatePurchaseOrderItem(Long id, PurchaseOrderItemRequestdto dto);

    void deletePurchaseOrderItem(Long id);

    PurchaseOrderItemResponsedto getPurchaseOrderItemById(Long id);

    List<PurchaseOrderItemResponsedto> getAllPurchaseOrderItems();

    List<PurchaseOrderItemResponsedto> getItemsByPurchaseOrder(Long purchaseOrderId);

    List<PurchaseOrderItemResponsedto> getItemsByProduct(Long productId);
}
