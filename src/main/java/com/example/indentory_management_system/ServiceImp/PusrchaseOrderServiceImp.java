package com.example.indentory_management_system.ServiceImp;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.indentory_management_system.Entity.PurchaseOrder;
import com.example.indentory_management_system.Entity.Supplier;
import com.example.indentory_management_system.Entity.Users;
import com.example.indentory_management_system.Exception.ResourceNotFoundException;
import com.example.indentory_management_system.Repository.*;
import com.example.indentory_management_system.Entity.PurchaseOrderItem;
import com.example.indentory_management_system.Entity.warehouses;
import com.example.indentory_management_system.Entity.Products;
import com.example.indentory_management_system.Service.PurchaseOrderService;
import com.example.indentory_management_system.Service.StockService;
import com.example.indentory_management_system.dto.PurchaseOrderRequestdto;
import com.example.indentory_management_system.dto.PurchaseOrderResponsedto;
import com.example.indentory_management_system.dto.StockRequestdto;
import com.example.indentory_management_system.Service.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PusrchaseOrderServiceImp implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseorderrepo;
    private final SupplierRepository supplierrepo;
    private final UserRepository userRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final StockService stockService;
    private final NotificationService notificationService;

    @Override
    public PurchaseOrderResponsedto addPurchaseOrder(PurchaseOrderRequestdto dto) {
        Supplier supplier = supplierrepo.findById(dto.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        Users user = userRepository.findById(dto.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        PurchaseOrder order = PurchaseOrder.builder()
                .poNumber(dto.getPoNumber())
                .supplier(supplier)
                .user(user)
                .orderedAt(dto.getOrderedAt())
                .expectedDeliveryDate(dto.getExpectedDeliveryDate())
                .totalAmount(dto.getTotalAmount())
                .status(dto.getStatus())
                .remarks(dto.getRemarks())
                .build();

        PurchaseOrder savedOrder = purchaseorderrepo.save(order);
        
        // Notify supplier if they have an associated user account
        if (supplier.getUser() != null) {
            String title = "New Purchase Order: " + savedOrder.getPoNumber();
            String message = "You have received a new purchase order for " + savedOrder.getTotalAmount() + ". Expected delivery: " + savedOrder.getExpectedDeliveryDate();
            notificationService.createNotification(supplier.getUser().getEmail(), title, message);
        }
        
        return toDto(savedOrder);
    }

    @Override
    public PurchaseOrderResponsedto updatePurchaseOrder(Long id, PurchaseOrderRequestdto dto) {
        PurchaseOrder order = purchaseorderrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order not found"));

        Supplier supplier = supplierrepo.findById(dto.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        Users user = userRepository.findById(dto.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        order.setPoNumber(dto.getPoNumber());
        order.setSupplier(supplier);
        order.setUser(user);
        order.setOrderedAt(dto.getOrderedAt());
        order.setExpectedDeliveryDate(dto.getExpectedDeliveryDate());
        order.setTotalAmount(dto.getTotalAmount());
        order.setStatus(dto.getStatus());
        order.setRemarks(dto.getRemarks());

        PurchaseOrder updatedOrder = purchaseorderrepo.save(order);
        return toDto(updatedOrder);
    }

    @Override
    public void deletePurchaseOrder(Long id) {
        PurchaseOrder order = purchaseorderrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order not found"));
        purchaseorderrepo.delete(order);
    }

    @Override
    public PurchaseOrderResponsedto findByPoNumber(String poNumber) {
        PurchaseOrder order = purchaseorderrepo.findByPoNumber(poNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order not found with PO number: " + poNumber));
        return toDto(order);
    }

    @Override
    public List<PurchaseOrderResponsedto> getAllPurchaseOrders() {
        return purchaseorderrepo.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PurchaseOrderResponsedto> findBySupplierId(Long supplierId) {
        return purchaseorderrepo.findBySupplierId(supplierId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PurchaseOrderResponsedto> findByStatus(String status) {
        return purchaseorderrepo.findByStatus(status)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PurchaseOrderResponsedto> findByCreatedBy(Long userId) {
        return purchaseorderrepo.findByUserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PurchaseOrderResponsedto> findByOrderDateRange(LocalDate from, LocalDate to) {
        return purchaseorderrepo.findByOrderedAtBetween(from, to)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PurchaseOrderResponsedto receivePurchaseOrder(Long id) {
        PurchaseOrder order = purchaseorderrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order not found with ID: " + id));

        if (!"SHIPPED".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException("Purchase order must be SHIPPED before it can be RECEIVED");
        }

        order.setStatus("RECEIVED");
        PurchaseOrder updatedOrder = purchaseorderrepo.save(order);

        warehouses defaultWarehouse = warehouseRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No warehouses available to receive stock"));

        List<PurchaseOrderItem> items = purchaseOrderItemRepository.findByPurchaseOrderId(id);
        for (PurchaseOrderItem item : items) {
            int qtyToReceive = item.getQuantityReceived() > 0 ? item.getQuantityReceived() : item.getQuantityOrdered();
            
            StockRequestdto stockRequest = StockRequestdto.builder()
                    .productId(item.getProduct().getId())
                    .warehouseId(defaultWarehouse.getId())
                    .quantityOnHand(qtyToReceive)
                    .build();
            stockService.addStock(stockRequest);

            Products product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + qtyToReceive);
            productRepository.save(product);
        }

        return toDto(updatedOrder);
    }

    @Override
    public PurchaseOrderResponsedto updateStatus(Long id, String status) {
        PurchaseOrder order = purchaseorderrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order not found with ID: " + id));

        if ("RECEIVED".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException("Cannot update status of a RECEIVED purchase order");
        }

        order.setStatus(status);
        PurchaseOrder updatedOrder = purchaseorderrepo.save(order);
        return toDto(updatedOrder);
    }

    @Override
    public PurchaseOrderResponsedto acceptOrder(Long id) {
        PurchaseOrder order = purchaseorderrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order not found with ID: " + id));

        if (!"ORDERED".equalsIgnoreCase(order.getStatus()) && !"PENDING".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException("Purchase order must be ORDERED or PENDING to accept.");
        }

        order.setStatus("ACCEPTED");
        return toDto(purchaseorderrepo.save(order));
    }

    @Override
    public PurchaseOrderResponsedto rejectOrder(Long id) {
        PurchaseOrder order = purchaseorderrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order not found with ID: " + id));

        if (!"ORDERED".equalsIgnoreCase(order.getStatus()) && !"PENDING".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException("Purchase order must be ORDERED or PENDING to reject.");
        }

        order.setStatus("REJECTED");
        return toDto(purchaseorderrepo.save(order));
    }

    @Override
    public PurchaseOrderResponsedto shipOrder(Long id) {
        PurchaseOrder order = purchaseorderrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order not found with ID: " + id));

        if (!"ACCEPTED".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException("Purchase order must be ACCEPTED to ship.");
        }

        order.setStatus("SHIPPED");
        return toDto(purchaseorderrepo.save(order));
    }

    private PurchaseOrderResponsedto toDto(PurchaseOrder order) {
        return PurchaseOrderResponsedto.builder()
                .id(order.getId())
                .poNumber(order.getPoNumber())
                .supplierName(order.getSupplier() != null ? order.getSupplier().getSupplierName() : null)
                .createdBy(order.getUser() != null ? order.getUser().getUsername() : null)
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderedAt(order.getOrderedAt())
                .expectedDeliveryDate(order.getExpectedDeliveryDate())
                .remarks(order.getRemarks())
                .build();
    }
}
