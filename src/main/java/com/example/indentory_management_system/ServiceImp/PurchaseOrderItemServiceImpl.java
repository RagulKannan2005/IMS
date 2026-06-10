package com.example.indentory_management_system.ServiceImp;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.indentory_management_system.Entity.Products;
import com.example.indentory_management_system.Entity.PurchaseOrder;
import com.example.indentory_management_system.Entity.PurchaseOrderItem;
import com.example.indentory_management_system.Entity.SupplierProduct;
import com.example.indentory_management_system.Exception.ResourceNotFoundException;
import com.example.indentory_management_system.Repository.ProductRepository;
import com.example.indentory_management_system.Repository.PurchaseOrderRepository;
import com.example.indentory_management_system.Repository.PurchaseOrderItemRepository;
import com.example.indentory_management_system.Repository.SupplierProductRepository;
import com.example.indentory_management_system.Service.PurchaseOrderItemService;
import com.example.indentory_management_system.dto.PurchaseOrderItemRequestdto;
import com.example.indentory_management_system.dto.PurchaseOrderItemResponsedto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseOrderItemServiceImpl implements PurchaseOrderItemService {

    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductRepository productRepository;
    private final SupplierProductRepository supplierProductRepository;

    @Override
    @Transactional
    public PurchaseOrderItemResponsedto addPurchaseOrderItem(PurchaseOrderItemRequestdto dto) {
        if (dto.getQuantityOrdered() <= 0) {
            throw new IllegalArgumentException("Quantity ordered must be greater than 0");
        }
        if (dto.getQuantityReceived() > dto.getQuantityOrdered()) {
            throw new IllegalArgumentException("Quantity received cannot exceed quantity ordered");
        }

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(dto.getPurchaseOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with ID: " + dto.getPurchaseOrderId()));

        Products product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + dto.getProductId()));

        SupplierProduct supplierProduct = supplierProductRepository.findBySupplierIdAndProductId(purchaseOrder.getSupplier().getId(), product.getId())
                .orElseThrow(() -> new IllegalArgumentException("Supplier does not supply this product"));

        if (dto.getQuantityOrdered() > supplierProduct.getAvailableQuantity()) {
            throw new IllegalArgumentException("Quantity ordered exceeds supplier's available quantity (" + supplierProduct.getAvailableQuantity() + ")");
        }

        BigDecimal totalCost = BigDecimal.valueOf(dto.getQuantityOrdered()).multiply(dto.getUnitCost());

        PurchaseOrderItem item = PurchaseOrderItem.builder()
                .purchaseOrder(purchaseOrder)
                .product(product)
                .quantityOrdered(dto.getQuantityOrdered())
                .quantityReceived(dto.getQuantityReceived())
                .unitCost(dto.getUnitCost())
                .totalCost(totalCost)
                .build();

        PurchaseOrderItem savedItem = purchaseOrderItemRepository.save(item);
        return toDto(savedItem);
    }

    @Override
    @Transactional
    public PurchaseOrderItemResponsedto updatePurchaseOrderItem(Long id, PurchaseOrderItemRequestdto dto) {
        PurchaseOrderItem item = purchaseOrderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order Item not found with ID: " + id));

        if (dto.getQuantityOrdered() <= 0) {
            throw new IllegalArgumentException("Quantity ordered must be greater than 0");
        }
        if (dto.getQuantityReceived() > dto.getQuantityOrdered()) {
            throw new IllegalArgumentException("Quantity received cannot exceed quantity ordered");
        }

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(dto.getPurchaseOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with ID: " + dto.getPurchaseOrderId()));

        Products product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + dto.getProductId()));

        SupplierProduct supplierProduct = supplierProductRepository.findBySupplierIdAndProductId(purchaseOrder.getSupplier().getId(), product.getId())
                .orElseThrow(() -> new IllegalArgumentException("Supplier does not supply this product"));

        if (dto.getQuantityOrdered() > supplierProduct.getAvailableQuantity()) {
            throw new IllegalArgumentException("Quantity ordered exceeds supplier's available quantity (" + supplierProduct.getAvailableQuantity() + ")");
        }

        BigDecimal totalCost = BigDecimal.valueOf(dto.getQuantityOrdered()).multiply(dto.getUnitCost());

        item.setPurchaseOrder(purchaseOrder);
        item.setProduct(product);
        item.setQuantityOrdered(dto.getQuantityOrdered());
        item.setQuantityReceived(dto.getQuantityReceived());
        item.setUnitCost(dto.getUnitCost());
        item.setTotalCost(totalCost);

        PurchaseOrderItem updatedItem = purchaseOrderItemRepository.save(item);
        return toDto(updatedItem);
    }

    @Override
    @Transactional
    public void deletePurchaseOrderItem(Long id) {
        PurchaseOrderItem item = purchaseOrderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order Item not found with ID: " + id));
        purchaseOrderItemRepository.delete(item);
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderItemResponsedto getPurchaseOrderItemById(Long id) {
        PurchaseOrderItem item = purchaseOrderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order Item not found with ID: " + id));
        return toDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderItemResponsedto> getAllPurchaseOrderItems() {
        return purchaseOrderItemRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderItemResponsedto> getItemsByPurchaseOrder(Long purchaseOrderId) {
        return purchaseOrderItemRepository.findByPurchaseOrderId(purchaseOrderId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderItemResponsedto> getItemsByProduct(Long productId) {
        return purchaseOrderItemRepository.findByProductId(productId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private PurchaseOrderItemResponsedto toDto(PurchaseOrderItem item) {
        return PurchaseOrderItemResponsedto.builder()
                .id(item.getId())
                .purchaseOrderId(item.getPurchaseOrder().getId())
                .poNumber(item.getPurchaseOrder().getPoNumber())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .quantityOrdered(item.getQuantityOrdered())
                .quantityReceived(item.getQuantityReceived())
                .unitCost(item.getUnitCost())
                .totalCost(item.getTotalCost())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }
}
