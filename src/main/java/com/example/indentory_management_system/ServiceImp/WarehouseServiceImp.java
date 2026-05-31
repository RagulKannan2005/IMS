package com.example.indentory_management_system.ServiceImp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.indentory_management_system.Entity.warehouses;
import com.example.indentory_management_system.Repository.WarehouseRepository;
import com.example.indentory_management_system.dto.warehousesResponsedto;
import com.example.indentory_management_system.Service.WarehouseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImp implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    @Override
    public warehousesResponsedto addwarehouses(warehousesResponsedto w) {
        warehouses warehouse = warehouses.builder()
                .name(w.getName())
                .warehouseCode(w.getWarehouseCode())
                .capacity(w.getCapacity())
                .managerName(w.getManagerName())
                .contactNumber(w.getContactNumber())
                .email(w.getEmail())
                .isActive(w.getIsActive())
                .build();
        warehouses saved = warehouseRepository.save(warehouse);
        return toDto(saved);
    }

    @Override
    public List<warehousesResponsedto> findbymanagername(String managerName) {
        return warehouseRepository.findbymanagername(managerName)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<warehousesResponsedto> findbyisActive(String status) {
        return warehouseRepository.findbyisActive(status)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<warehousesResponsedto> findbywarehousename(String name) {
        return warehouseRepository.findbywarehousename(name)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public warehousesResponsedto findBywarehouseCode(String warehouseCode) {
        return warehouseRepository.findBywarehouseCode(warehouseCode)
                .map(this::toDto).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        warehouseRepository.deleteById(id);
    }

    @Override
    public warehousesResponsedto updatewarehouse(Long id, warehousesResponsedto w) {
        warehouses warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + id));
        warehouse.setName(w.getName());
        warehouse.setWarehouseCode(w.getWarehouseCode());
        warehouse.setCapacity(w.getCapacity());
        warehouse.setManagerName(w.getManagerName());
        warehouse.setContactNumber(w.getContactNumber());
        warehouse.setEmail(w.getEmail());
        warehouse.setIsActive(w.getIsActive());
        warehouses saved = warehouseRepository.save(warehouse);
        return toDto(saved);
    }

    private warehousesResponsedto toDto(warehouses s) {
        return warehousesResponsedto.builder()
                .id(s.getId())
                .name(s.getName())
                .warehouseCode(s.getWarehouseCode())
                .capacity(s.getCapacity())
                .managerName(s.getManagerName())
                .contactNumber(s.getContactNumber())
                .email(s.getEmail())
                .isActive(s.getIsActive())
                .build();

    }

}
