package com.example.indentory_management_system.Service;

import java.util.List;

import com.example.indentory_management_system.dto.StockRequestdto;
import com.example.indentory_management_system.dto.StockResponsedto;

public interface StockService {

    StockResponsedto addStock(StockRequestdto dto);

    List<StockResponsedto> getAllStocks();

    // StockResponsedto stockOut(Long id, StockRequestdto dto);

    StockResponsedto updateStock(Long id, StockRequestdto dto);

    void deleteStock(Long id);

    // StockResponsedto adjustStock(Long id, int quantity);

    List<StockResponsedto> transferStock(Long fromWarehouseId, Long toWarehouseId, StockRequestdto dto);

    StockResponsedto getCurrentStock(Long productId, Long warehouseId);

    // List<StockResponsedto> getStockByProduct(String productName);

    // List<StockResponsedto> getStockByWarehouse(String warehouseName);
}