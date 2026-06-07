package com.example.indentory_management_system.ServiceImp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.indentory_management_system.Entity.Products;
import com.example.indentory_management_system.Entity.Stock;
import com.example.indentory_management_system.Entity.warehouses;
import com.example.indentory_management_system.Exception.ResourceNotFoundException;
import com.example.indentory_management_system.Repository.ProductRepository;
import com.example.indentory_management_system.Repository.StockRepository;
import com.example.indentory_management_system.Repository.WarehouseRepository;
import com.example.indentory_management_system.Service.StockService;
import com.example.indentory_management_system.dto.StockRequestdto;
import com.example.indentory_management_system.dto.StockResponsedto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockServiceImp implements StockService {

    private final StockRepository stockrepo;
    private final ProductRepository productrepo;
    private final WarehouseRepository warehouserepo;

    @Override
    public StockResponsedto addStock(StockRequestdto dto) {
        Products product = productrepo.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        warehouses warehouse = warehouserepo.findById(dto.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));

        Stock stock = stockrepo.findByProductsIdAndWarehousesId(dto.getProductId(), dto.getWarehouseId())
                .map(existingStock -> {
                    existingStock.setQuantityOnHand(existingStock.getQuantityOnHand() + dto.getQuantityOnHand());
                    existingStock.setUpdatedAt(LocalDateTime.now());
                    return existingStock;
                })
                .orElseGet(() -> Stock.builder()
                        .Products(product)
                        .warehouses(warehouse)
                        .quantityOnHand(dto.getQuantityOnHand())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build());

        stockrepo.save(stock);
        return toDto(stock);
    }

    @Override
    public StockResponsedto updateStock(Long id, StockRequestdto dto) {
        Stock stock = stockrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found"));
        Products product = productrepo.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        warehouses warehouse = warehouserepo.findById(dto.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));
        stock.setProducts(product);
        stock.setWarehouses(warehouse);
        stock.setQuantityOnHand(dto.getQuantityOnHand());
        stock.setUpdatedAt(LocalDateTime.now());
        stockrepo.save(stock);
        return toDto(stock);
    }

    @Override
    public List<StockResponsedto> getAllStocks() {
        return stockrepo.findAll()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public void deleteStock(Long id) {
        Stock stock = stockrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("stock is not found"));

        stockrepo.delete(stock);
    }

    @Override
    public List<StockResponsedto> transferStock(
            Long fromWarehouseId,
            Long toWarehouseId,
            StockRequestdto dto) {

        Stock sourceStock = stockrepo
                .findByProductsIdAndWarehousesId(
                        dto.getProductId(),
                        fromWarehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Source stock not found"));

        Stock destinationStock = stockrepo
                .findByProductsIdAndWarehousesId(
                        dto.getProductId(),
                        toWarehouseId)
                .orElseGet(() -> {
                    Products product = productrepo.findById(dto.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
                    warehouses destinationWarehouse = warehouserepo.findById(toWarehouseId)
                            .orElseThrow(() -> new ResourceNotFoundException("Destination warehouse not found"));
                    return Stock.builder()
                            .Products(product)
                            .warehouses(destinationWarehouse)
                            .quantityOnHand(0)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                });

        int transferQty = dto.getQuantityOnHand();

        if (sourceStock.getQuantityOnHand() < transferQty) {
            throw new RuntimeException("Insufficient stock in source warehouse");
        }

        // Reduce stock from source warehouse
        sourceStock.setQuantityOnHand(
                sourceStock.getQuantityOnHand() - transferQty);

        // Add stock to destination warehouse
        destinationStock.setQuantityOnHand(
                destinationStock.getQuantityOnHand() + transferQty);

        stockrepo.save(sourceStock);
        stockrepo.save(destinationStock);

        return List.of(
                toDto(sourceStock),
                toDto(destinationStock));
    }

    private StockResponsedto toDto(Stock stock) {
        return StockResponsedto.builder()
                .id(stock.getId())
                .productName(stock.getProducts().getName())
                .warehouseName(stock.getWarehouses().getName())
                .quantityOnHand(stock.getQuantityOnHand())
                .build();
    }

    @Override
    public StockResponsedto getCurrentStock(Long productid, Long warehouseid) {
        Stock stock = stockrepo.findByProductsIdAndWarehousesId(productid, warehouseid)
                .orElseThrow(() -> new ResourceNotFoundException("stock is not found"));
        return toDto(stock);
    }

}
