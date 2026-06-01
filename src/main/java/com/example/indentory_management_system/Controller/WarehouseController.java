package com.example.indentory_management_system.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<warehousesResponsedto> addwarehouses(@Valid @RequestBody warehousesRequestdto dto){
        warehousesResponsedto added = warehouseService.addwarehouses(dto);
        return ResponseEntity.ok(added);
    }

    @GetMapping("/warehouse_code/{code}")
    public ResponseEntity<warehousesResponsedto> findBywarehouseCode(@PathVariable String code){
        warehousesResponsedto found = warehouseService.findBywarehouseCode(code);
        return ResponseEntity.ok(found);
    }

    @GetMapping("managername/{name}")
    public ResponseEntity<List<warehousesResponsedto>> findbymanagername(@PathVariable String name){
        List<warehousesResponsedto> found = warehouseService.findbymanagername(name);
        return ResponseEntity.ok(found);
    }

    @GetMapping("isactive/{status}")
    public ResponseEntity<List<warehousesResponsedto>> findbyisActive(@PathVariable String status){
        List<warehousesResponsedto> found = warehouseService.findbyisActive(status);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<warehousesResponsedto>> findbywarehousename(@PathVariable String name){
        List<warehousesResponsedto> found = warehouseService.findbywarehousename(name);
        return ResponseEntity.ok(found);
    }

    @PutMapping("/updatewarehouse/{id}")
    public ResponseEntity<warehousesResponsedto> updatewarehouse(
            @PathVariable Long id,
            @Valid @RequestBody warehousesRequestdto w) {
        return ResponseEntity.ok(warehouseService.updatewarehouse(id, w));
    }

    @DeleteMapping("/deletewarehouse/{id}")
    public ResponseEntity<warehousesResponsedto> deleteById(@PathVariable Long id) {
        warehouseService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
