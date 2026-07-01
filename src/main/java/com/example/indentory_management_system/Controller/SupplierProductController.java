package com.example.indentory_management_system.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.indentory_management_system.Service.SupplierProductService;
import com.example.indentory_management_system.dto.SupplierProductRequestdto;
import com.example.indentory_management_system.dto.SupplierProductResponsedto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/supplier-products")
@RequiredArgsConstructor
@RestController
public class SupplierProductController {

    private final SupplierProductService supplierProductService;

    @PostMapping("/addproduct")
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<SupplierProductResponsedto> addproduct(@Valid @RequestBody SupplierProductRequestdto dto) {
        return ResponseEntity.ok(supplierProductService.createSupplierProduct(dto));
    }

    @GetMapping("/allproducts")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF', 'SUPPLIER')")
    public ResponseEntity<List<SupplierProductResponsedto>> getallproducts() {
        return ResponseEntity.ok(supplierProductService.getAllSupplierProducts());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF', 'SUPPLIER')")
    public ResponseEntity<SupplierProductResponsedto> getbyid(@PathVariable Long id) {
        return ResponseEntity.ok(supplierProductService.getSupplierProductById(id));
    }

    @PutMapping("/updateproduct/{id}")
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<SupplierProductResponsedto> updateproduct(@PathVariable Long id,
            @Valid @RequestBody SupplierProductRequestdto dto) {
        return ResponseEntity.ok(supplierProductService.updateSupplierProduct(id, dto));
    }

    @DeleteMapping("/deleteproduct/{id}")
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<SupplierProductResponsedto> deleteproduct(@PathVariable Long id) {
        return ResponseEntity.ok(supplierProductService.deleteSupplierProduct(id));
    }

    @GetMapping("/supplier/{supplierId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF') or (hasRole('SUPPLIER') and @securityService.isOwnSupplierId(authentication, #supplierId))")
    public ResponseEntity<List<SupplierProductResponsedto>> getProductsBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(supplierProductService.getProductsBySupplierId(supplierId));
    }
}
