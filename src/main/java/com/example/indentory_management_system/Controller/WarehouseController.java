package com.example.indentory_management_system.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.indentory_management_system.Service.WarehouseService;
import com.example.indentory_management_system.dto.warehousesRequestdto;
import com.example.indentory_management_system.dto.warehousesResponsedto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/warehouses")
@RequiredArgsConstructor
public class WarehouseController {
    
    private final WarehouseService warehouseService;

    @PostMapping("/addwarehouse")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<warehousesResponsedto> addwarehouses(@Valid @RequestBody warehousesRequestdto dto){
        warehousesResponsedto added = warehouseService.addwarehouses(dto);
        return ResponseEntity.ok(added);
    }

    @GetMapping("/warehouse_code/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<warehousesResponsedto> findBywarehouseCode(@PathVariable String code){
        warehousesResponsedto found = warehouseService.findBywarehouseCode(code);
        return ResponseEntity.ok(found);
    }

    @GetMapping("managername/{name}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<List<warehousesResponsedto>> findbymanagername(@PathVariable String name){
        List<warehousesResponsedto> found = warehouseService.findbymanagername(name);
        return ResponseEntity.ok(found);
    }

    @GetMapping("isactive/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<List<warehousesResponsedto>> findbyisActive(@PathVariable String status){
        List<warehousesResponsedto> found = warehouseService.findbyisActive(status);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<List<warehousesResponsedto>> findbywarehousename(@PathVariable String name){
        List<warehousesResponsedto> found = warehouseService.findbywarehousename(name);
        return ResponseEntity.ok(found);
    }

    @PutMapping("/updatewarehouse/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<warehousesResponsedto> updatewarehouse(
            @PathVariable Long id,
            @Valid @RequestBody warehousesRequestdto w) {
        return ResponseEntity.ok(warehouseService.updatewarehouse(id, w));
    }

    @DeleteMapping("/deletewarehouse/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<warehousesResponsedto> deleteById(@PathVariable Long id) {
        warehouseService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
