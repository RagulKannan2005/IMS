package com.example.indentory_management_system.ServiceImp;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.indentory_management_system.Entity.PurchaseOrder;
import com.example.indentory_management_system.Entity.Supplier;
import com.example.indentory_management_system.Entity.Users;
import com.example.indentory_management_system.Exception.ResourceNotFoundException;
import com.example.indentory_management_system.Repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.indentory_management_system.Entity.PurchaseOrderItem;
import com.example.indentory_management_system.Entity.warehouses;
import com.example.indentory_management_system.Entity.Products;
import com.example.indentory_management_system.Service.PurchaseOrderService;
import com.example.indentory_management_system.Service.StockService;
import com.example.indentory_management_system.dto.PurchaseOrderRequestdto;
import com.example.indentory_management_system.dto.PurchaseOrderResponsedto;
import com.example.indentory_management_system.dto.StockRequestdto;

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
    private final SupplierProductRepository supplierProductRepository;
    private final com.example.indentory_management_system.Service.StockMovementService stockMovementService;
    private final com.example.indentory_management_system.Repository.SupplierProductMappingRepository supplierProductMappingRepository;

    @Override
    @Transactional
    public PurchaseOrderResponsedto addPurchaseOrder(PurchaseOrderRequestdto dto) {
        Supplier supplier = supplierrepo.findById(dto.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        warehouses warehouse = warehouseRepository.findById(dto.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Current authenticated user not found"));

        PurchaseOrder order = PurchaseOrder.builder()
                .poNumber(dto.getPoNumber())
                .supplier(supplier)
                .warehouse(warehouse)
                .user(user)
                .orderedAt(dto.getOrderedAt())
                .expectedDeliveryDate(dto.getExpectedDeliveryDate())
                .totalAmount(dto.getTotalAmount())
                .orderStatus(dto.getStatus())
                .remarks(dto.getRemarks())
                .build();

        PurchaseOrder savedOrder = purchaseorderrepo.save(order);

        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            for (com.example.indentory_management_system.dto.PurchaseOrderItemRequestdto itemDto : dto.getItems()) {
                com.example.indentory_management_system.Entity.SupplierProduct supplierProduct = supplierProductRepository.findById(itemDto.getSupplierProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Supplier Product not found"));

                PurchaseOrderItem item = PurchaseOrderItem.builder()
                        .purchaseOrder(savedOrder)
                        .supplierProduct(supplierProduct)
                        .quantityOrdered(itemDto.getQuantityOrdered())
                        .quantityReceived(itemDto.getQuantityReceived())
                        .unitCost(itemDto.getUnitCost())
                        .totalCost(itemDto.getUnitCost().multiply(java.math.BigDecimal.valueOf(itemDto.getQuantityOrdered())))
                        .build();

                purchaseOrderItemRepository.save(item);
            }
        }

        return toDto(savedOrder);
    }

    @Override
    @Transactional
    public PurchaseOrderResponsedto updatePurchaseOrder(Long id, PurchaseOrderRequestdto dto) {
        PurchaseOrder order = purchaseorderrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order not found"));

        Supplier supplier = supplierrepo.findById(dto.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        warehouses warehouse = warehouseRepository.findById(dto.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Current authenticated user not found"));

        order.setPoNumber(dto.getPoNumber());
        order.setSupplier(supplier);
        order.setWarehouse(warehouse);
        order.setUser(user);
        order.setOrderedAt(dto.getOrderedAt());
        order.setExpectedDeliveryDate(dto.getExpectedDeliveryDate());
        order.setTotalAmount(dto.getTotalAmount());
        order.setOrderStatus(dto.getStatus());
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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users currentUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Current authenticated user not found"));

        List<PurchaseOrder> orders = purchaseorderrepo.findByUserId(currentUser.getId());

        return orders.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PurchaseOrderResponsedto> findBySupplierId(Long supplierId) {
        List<PurchaseOrder> orders = purchaseorderrepo.findBySupplierId(supplierId);

        return orders.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PurchaseOrderResponsedto> findByStatus(String status) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users currentUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Current authenticated user not found"));

        List<PurchaseOrder> orders = purchaseorderrepo.findByStatusAndUserId(status, currentUser.getId());

        return orders.stream()
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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users currentUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Current authenticated user not found"));

        List<PurchaseOrder> orders = purchaseorderrepo.findByOrderedAtBetweenAndUserId(from, to, currentUser.getId());

        return orders.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PurchaseOrderResponsedto receivePurchaseOrder(com.example.indentory_management_system.dto.PurchaseOrderReceiveDto dto) {
        Long id = dto.getPurchaseOrderId();
        PurchaseOrder order = purchaseorderrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order not found with ID: " + id));

        if ("RECEIVED".equalsIgnoreCase(order.getOrderStatus())) {
            throw new RuntimeException("Purchase order is already RECEIVED");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users currentUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Current authenticated user not found"));

        if (dto.getMappings() != null) {
            for (com.example.indentory_management_system.dto.ProductMappingDto mapping : dto.getMappings()) {
                PurchaseOrderItem item = purchaseOrderItemRepository.findById(mapping.getPurchaseOrderItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("PO Item not found"));
                
                com.example.indentory_management_system.Entity.SupplierProduct sp = item.getSupplierProduct();
                Products internalProduct = null;

                if ("CREATE".equalsIgnoreCase(mapping.getAction())) {
                    internalProduct = Products.builder()
                            .sku(sp.getSku())
                            .name(sp.getName())
                            .description(sp.getDescription())
                            .categories(sp.getCategories())
                            .costPrice(sp.getCostPrice())
                            .sellingPrice(sp.getCostPrice() * 1.5) // Default selling price
                            .stockQuantity(0)
                            .reorderLevel(10)
                            .reorderQuantity(50)
                            .user(currentUser)
                            .build();
                    internalProduct = productRepository.save(internalProduct);
                    
                    com.example.indentory_management_system.Entity.SupplierProductMapping spMapping = com.example.indentory_management_system.Entity.SupplierProductMapping.builder()
                            .supplierProduct(sp)
                            .internalProduct(internalProduct)
                            .createdBy(currentUser.getId())
                            .build();
                    supplierProductMappingRepository.save(spMapping);

                } else if ("LINK".equalsIgnoreCase(mapping.getAction())) {
                    internalProduct = productRepository.findById(mapping.getInternalProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Internal product not found"));
                    
                    java.util.Optional<com.example.indentory_management_system.Entity.SupplierProductMapping> existingMapping = supplierProductMappingRepository.findBySupplierProductId(sp.getId());
                    if (existingMapping.isEmpty()) {
                        com.example.indentory_management_system.Entity.SupplierProductMapping spMapping = com.example.indentory_management_system.Entity.SupplierProductMapping.builder()
                                .supplierProduct(sp)
                                .internalProduct(internalProduct)
                                .createdBy(currentUser.getId())
                                .build();
                        supplierProductMappingRepository.save(spMapping);
                    }
                }
                
                if (internalProduct != null) {
                    com.example.indentory_management_system.dto.StockMovementRequestDto movementDto = new com.example.indentory_management_system.dto.StockMovementRequestDto();
                    movementDto.setProduct_id(internalProduct.getId());
                    movementDto.setWarehouse_id(order.getWarehouse().getId());
                    movementDto.setQuantity(item.getQuantityOrdered());
                    movementDto.setMovement_type("IN");
                    movementDto.setReference_no(order.getPoNumber());
                    movementDto.setRemarks("Purchase Order Received");
                    movementDto.setPerformed_by(currentUser.getId());
                    stockMovementService.createMovement(movementDto);
                }
            }
        }

        order.setOrderStatus("RECEIVED");
        PurchaseOrder updatedOrder = purchaseorderrepo.save(order);

        return toDto(updatedOrder);
    }

    @Override
    public PurchaseOrderResponsedto updateStatus(Long id, String status) {
        PurchaseOrder order = purchaseorderrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order not found with ID: " + id));

        if ("RECEIVED".equalsIgnoreCase(order.getOrderStatus())) {
            throw new RuntimeException("Cannot update status of a RECEIVED purchase order");
        }

        order.setOrderStatus(status);
        PurchaseOrder updatedOrder = purchaseorderrepo.save(order);
        return toDto(updatedOrder);
    }

    private PurchaseOrderResponsedto toDto(PurchaseOrder order) {
        java.util.List<com.example.indentory_management_system.dto.PurchaseOrderItemResponsedto> itemDtos = new java.util.ArrayList<>();
        if (order.getItems() != null) {
            itemDtos = order.getItems().stream().map(item -> com.example.indentory_management_system.dto.PurchaseOrderItemResponsedto.builder()
                    .id(item.getId())
                    .purchaseOrderId(order.getId())
                    .poNumber(order.getPoNumber())
                    .supplierProductId(item.getSupplierProduct() != null ? item.getSupplierProduct().getId() : null)
                    .supplierProductName(item.getSupplierProduct() != null ? item.getSupplierProduct().getName() : null)
                    .quantityOrdered(item.getQuantityOrdered())
                    .quantityReceived(item.getQuantityReceived())
                    .unitCost(item.getUnitCost())
                    .totalCost(item.getTotalCost())
                    .build()).collect(Collectors.toList());
        }

        return PurchaseOrderResponsedto.builder()
                .id(order.getId())
                .poNumber(order.getPoNumber())
                .supplierName(order.getSupplier() != null ? order.getSupplier().getSupplierName() : null)
                .warehouseName(order.getWarehouse() != null ? order.getWarehouse().getName() : null)
                .createdBy(order.getUser() != null ? order.getUser().getUsername() : null)
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .orderedAt(order.getOrderedAt())
                .expectedDeliveryDate(order.getExpectedDeliveryDate())
                .remarks(order.getRemarks())
                .items(itemDtos)
                .build();
    }
}
