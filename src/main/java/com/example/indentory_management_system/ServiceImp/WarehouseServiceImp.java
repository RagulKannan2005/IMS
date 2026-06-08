package com.example.indentory_management_system.ServiceImp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.indentory_management_system.Entity.warehouses;
import com.example.indentory_management_system.Repository.WarehouseRepository;
import com.example.indentory_management_system.dto.warehousesRequestdto;
import com.example.indentory_management_system.dto.warehousesResponsedto;
import com.example.indentory_management_system.Service.WarehouseService;
import com.example.indentory_management_system.Entity.Users;
import com.example.indentory_management_system.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImp implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;

    @Override
    public warehousesResponsedto addwarehouses(warehousesRequestdto w) {
        Users user = null;
        if (w.getUserId() != null) {
            user = userRepository.findById(w.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + w.getUserId()));
        }

        warehouses warehouse = warehouses.builder()
                .name(w.getName())
                .warehouseCode(w.getWarehouseCode())
                .capacity(w.getCapacity())
                .managerName(w.getManagerName())
                .contactNumber(w.getContactNumber())
                .email(w.getEmail())
                .isActive("active")
                .user(user)
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
    public warehousesResponsedto updatewarehouse(Long id, warehousesRequestdto w) {
        warehouses warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + id));

        Users user = null;
        if (w.getUserId() != null) {
            user = userRepository.findById(w.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + w.getUserId()));
        }

        warehouse.setName(w.getName());
        warehouse.setWarehouseCode(w.getWarehouseCode());
        warehouse.setCapacity(w.getCapacity());
        warehouse.setManagerName(w.getManagerName());
        warehouse.setContactNumber(w.getContactNumber());
        warehouse.setEmail(w.getEmail());
        warehouse.setUser(user);
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
                .createdDate(s.getCreatedDate())
                .updatedDate(s.getUpdatedDate())
                .userId(s.getUser() != null ? s.getUser().getId() : null)
                .build();
    }

    @Override
    public List<warehousesResponsedto> getAllWarehouses() {
        return warehouseRepository.findAll().stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<warehousesResponsedto> getWarehousesByUser(Long userId) {
        return warehouseRepository.findByUserId(userId).stream()
                .map(this::toDto).collect(Collectors.toList());
    }
}
