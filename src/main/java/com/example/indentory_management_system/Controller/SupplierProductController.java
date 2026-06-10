package com.example.indentory_management_system.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.indentory_management_system.Service.SupplierProductService;
import com.example.indentory_management_system.dto.SupplierProductDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/supplier-products")
@RequiredArgsConstructor
public class SupplierProductController {

    private final SupplierProductService supplierProductService;

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<SupplierProductDto>> getSupplierProducts(@PathVariable Long supplierId) {
        return ResponseEntity.ok(supplierProductService.getSupplierProductsBySupplierId(supplierId));
    }
}
