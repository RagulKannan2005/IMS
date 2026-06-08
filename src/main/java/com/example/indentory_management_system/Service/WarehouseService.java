package com.example.indentory_management_system.Service;

import java.util.List;
import com.example.indentory_management_system.Entity.warehouses;
import com.example.indentory_management_system.dto.warehousesRequestdto;
import com.example.indentory_management_system.dto.warehousesResponsedto;

public interface WarehouseService {
    public List<warehousesResponsedto> findbymanagername(String managerName);

    public List<warehousesResponsedto> findbyisActive(String status);

    public List<warehousesResponsedto> findbywarehousename(String name);

    public warehousesResponsedto addwarehouses(warehousesRequestdto w);

    public warehousesResponsedto findBywarehouseCode(String warehouseCode);

    public void deleteById(Long id);

    public warehousesResponsedto updatewarehouse(Long id, warehousesRequestdto w);

    public List<warehousesResponsedto> getAllWarehouses();

    public List<warehousesResponsedto> getWarehousesByUser(Long userId);
}
