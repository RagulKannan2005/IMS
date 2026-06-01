package com.example.indentory_management_system.ServiceImp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.indentory_management_system.Entity.Products;
import com.example.indentory_management_system.Entity.Stock;
import com.example.indentory_management_system.Entity.StockMovement;
import com.example.indentory_management_system.Entity.Users;
import com.example.indentory_management_system.Entity.warehouses;
import com.example.indentory_management_system.Exception.ResourceNotFoundException;
import com.example.indentory_management_system.Repository.ProductRepository;
import com.example.indentory_management_system.Repository.StockMovementRepository;
import com.example.indentory_management_system.Repository.StockRepository;
import com.example.indentory_management_system.Repository.UserRepository;
import com.example.indentory_management_system.Repository.WarehouseRepository;
import com.example.indentory_management_system.Service.StockMovementService;
import com.example.indentory_management_system.dto.StockMovementRequestDto;
import com.example.indentory_management_system.dto.StockMovementResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockMovementServiceImpl implements StockMovementService {

    private final StockMovementRepository stockMovementRepo;
    private final ProductRepository productRepo;
    private final WarehouseRepository warehouseRepo;
    private final UserRepository userRepo;
    private final StockRepository stockRepo;

    @Override
    @Transactional
    public StockMovementResponseDto createMovement(StockMovementRequestDto dto) {
        Products product = productRepo.findById(dto.getProduct_id())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        warehouses warehouse = warehouseRepo.findById(dto.getWarehouse_id())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));

        // Determine who performed the action
        Users user = null;
        if (dto.getPerformed_by() != null) {
            user = userRepo.findById(dto.getPerformed_by())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        } else {
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            user = userRepo.findByUsername(currentUsername)
                    .orElseThrow(() -> new ResourceNotFoundException("Current authenticated user not found"));
        }

        String type = dto.getMovement_type().toUpperCase();
        Integer qty = dto.getQuantity();
        String refNo = dto.getReference_no() != null ? dto.getReference_no() : "REF-" + UUID.randomUUID().toString().substring(0, 8);

        StockMovement mainMovement = null;

        if ("IN".equals(type)) {
            // Adjust Physical Stock (IN)
            Stock stock = stockRepo.findByProductsIdAndWarehousesId(dto.getProduct_id(), dto.getWarehouse_id())
                    .map(existingStock -> {
                        existingStock.setQuantityOnHand(existingStock.getQuantityOnHand() + qty);
                        existingStock.setUpdatedAt(LocalDateTime.now());
                        return existingStock;
                    })
                    .orElseGet(() -> Stock.builder()
                            .Products(product)
                            .warehouses(warehouse)
                            .quantityOnHand(qty)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build());
            stockRepo.save(stock);

            // Record Movement History
            mainMovement = StockMovement.builder()
                    .products(product)
                    .warehouses(warehouse)
                    .movementType("IN")
                    .quantity(qty)
                    .referenceNo(refNo)
                    .performedBy(user)
                    .remarks(dto.getRemarks())
                    .build();
            stockMovementRepo.save(mainMovement);

        } else if ("OUT".equals(type)) {
            // Adjust Physical Stock (OUT)
            Stock stock = stockRepo.findByProductsIdAndWarehousesId(dto.getProduct_id(), dto.getWarehouse_id())
                    .orElseThrow(() -> new RuntimeException("Stock record not found in warehouse"));

            if (stock.getQuantityOnHand() < qty) {
                throw new RuntimeException("Insufficient stock in warehouse to perform OUT movement");
            }

            stock.setQuantityOnHand(stock.getQuantityOnHand() - qty);
            stock.setUpdatedAt(LocalDateTime.now());
            stockRepo.save(stock);

            // Record Movement History
            mainMovement = StockMovement.builder()
                    .products(product)
                    .warehouses(warehouse)
                    .movementType("OUT")
                    .quantity(qty)
                    .referenceNo(refNo)
                    .performedBy(user)
                    .remarks(dto.getRemarks())
                    .build();
            stockMovementRepo.save(mainMovement);

        } else if ("ADJUSTMENT".equals(type)) {
            // Adjust Physical Stock
            Stock stock = stockRepo.findByProductsIdAndWarehousesId(dto.getProduct_id(), dto.getWarehouse_id())
                    .map(existingStock -> {
                        existingStock.setQuantityOnHand(existingStock.getQuantityOnHand() + qty);
                        existingStock.setUpdatedAt(LocalDateTime.now());
                        return existingStock;
                    })
                    .orElseGet(() -> {
                        if (qty < 0) {
                            throw new RuntimeException("Cannot create new stock with a negative adjustment quantity");
                        }
                        return Stock.builder()
                                .Products(product)
                                .warehouses(warehouse)
                                .quantityOnHand(qty)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();
                    });
            stockRepo.save(stock);

            // Record Movement History
            mainMovement = StockMovement.builder()
                    .products(product)
                    .warehouses(warehouse)
                    .movementType("ADJUSTMENT")
                    .quantity(qty)
                    .referenceNo(refNo)
                    .performedBy(user)
                    .remarks(dto.getRemarks())
                    .build();
            stockMovementRepo.save(mainMovement);

        } else if ("TRANSFER".equals(type)) {
            if (dto.getToWarehouseId() == null) {
                throw new RuntimeException("Destination toWarehouseId is required for TRANSFER type");
            }

            warehouses toWarehouse = warehouseRepo.findById(dto.getToWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Destination warehouse not found"));

            // Get source stock
            Stock sourceStock = stockRepo.findByProductsIdAndWarehousesId(dto.getProduct_id(), dto.getWarehouse_id())
                    .orElseThrow(() -> new ResourceNotFoundException("Source stock record not found"));

            if (sourceStock.getQuantityOnHand() < qty) {
                throw new RuntimeException("Insufficient stock in source warehouse to perform TRANSFER");
            }

            // Get destination stock
            Stock destinationStock = stockRepo.findByProductsIdAndWarehousesId(dto.getProduct_id(), dto.getToWarehouseId())
                    .orElseGet(() -> Stock.builder()
                            .Products(product)
                            .warehouses(toWarehouse)
                            .quantityOnHand(0)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build());

            // Deduct and Add
            sourceStock.setQuantityOnHand(sourceStock.getQuantityOnHand() - qty);
            destinationStock.setQuantityOnHand(destinationStock.getQuantityOnHand() + qty);

            stockRepo.save(sourceStock);
            stockRepo.save(destinationStock);

            // Record TWO movements (OUT from source, IN to destination) for double-entry tracking
            StockMovement outMovement = StockMovement.builder()
                    .products(product)
                    .warehouses(warehouse)
                    .movementType("TRANSFER_OUT")
                    .quantity(qty)
                    .referenceNo(refNo)
                    .performedBy(user)
                    .remarks("Transfer to warehouse: " + toWarehouse.getName() + ". " + (dto.getRemarks() != null ? dto.getRemarks() : ""))
                    .build();
            stockMovementRepo.save(outMovement);

            StockMovement inMovement = StockMovement.builder()
                    .products(product)
                    .warehouses(toWarehouse)
                    .movementType("TRANSFER_IN")
                    .quantity(qty)
                    .referenceNo(refNo)
                    .performedBy(user)
                    .remarks("Transfer from warehouse: " + warehouse.getName() + ". " + (dto.getRemarks() != null ? dto.getRemarks() : ""))
                    .build();
            stockMovementRepo.save(inMovement);

            mainMovement = outMovement; // Return the source OUT movement as primary response

        } else {
            throw new IllegalArgumentException("Invalid movement type. Must be IN, OUT, ADJUSTMENT, or TRANSFER");
        }

        return toDto(mainMovement);
    }

    @Override
    public StockMovementResponseDto getMovementById(Long id) {
        StockMovement movement = stockMovementRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock movement not found with id: " + id));
        return toDto(movement);
    }

    @Override
    public List<StockMovementResponseDto> getAllMovements() {
        return stockMovementRepo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockMovementResponseDto> getMovementsByProduct(Long productId) {
        return stockMovementRepo.findByProductsId(productId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockMovementResponseDto> getMovementsByWarehouse(Long warehouseId) {
        return stockMovementRepo.findByWarehousesId(warehouseId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockMovementResponseDto> getMovementsByType(String type) {
        return stockMovementRepo.findByMovementType(type.toUpperCase()).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockMovementResponseDto> getMovementsByDateRange(LocalDateTime start, LocalDateTime end) {
        return stockMovementRepo.findByCreatedAtBetween(start, end).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StockMovementResponseDto updateMovement(Long id, StockMovementRequestDto dto) {
        StockMovement movement = stockMovementRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock movement not found with id: " + id));

        Products product = productRepo.findById(dto.getProduct_id())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        warehouses warehouse = warehouseRepo.findById(dto.getWarehouse_id())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));

        movement.setProducts(product);
        movement.setWarehouses(warehouse);
        movement.setMovementType(dto.getMovement_type().toUpperCase());
        movement.setQuantity(dto.getQuantity());
        movement.setReferenceNo(dto.getReference_no());
        movement.setRemarks(dto.getRemarks());

        stockMovementRepo.save(movement);
        return toDto(movement);
    }

    @Override
    @Transactional
    public void deleteMovement(Long id) {
        StockMovement movement = stockMovementRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock movement not found with id: " + id));
        stockMovementRepo.delete(movement);
    }

    private StockMovementResponseDto toDto(StockMovement movement) {
        return StockMovementResponseDto.builder()
                .id(movement.getId())
                .productId(movement.getProducts().getId())
                .productName(movement.getProducts().getName())
                .warehouseId(movement.getWarehouses().getId())
                .warehouseName(movement.getWarehouses().getName())
                .movementType(movement.getMovementType())
                .quantity(movement.getQuantity())
                .referenceNo(movement.getReferenceNo())
                .performedById(movement.getPerformedBy().getId())
                .performedByUsername(movement.getPerformedBy().getUsername())
                .remarks(movement.getRemarks())
                .createdAt(movement.getCreatedAt())
                .build();
     }
}
