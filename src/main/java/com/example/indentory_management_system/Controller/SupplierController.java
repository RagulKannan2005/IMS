package com.example.indentory_management_system.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.indentory_management_system.Entity.Users;
import com.example.indentory_management_system.Exception.ResourceNotFoundException;
import com.example.indentory_management_system.Repository.UserRepository;
import com.example.indentory_management_system.Service.ProductService;
import com.example.indentory_management_system.Service.PurchaseOrderService;
import com.example.indentory_management_system.Service.SupplierService;
import com.example.indentory_management_system.dto.*;

import java.util.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/suppliers")
public class SupplierController {
    
    private final SupplierService supplierService;
    private final ProductService productService;
    private final PurchaseOrderService purchaseOrderService;
    private final UserRepository userRepository;

    @PostMapping("/addsupplier")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<SupplierResponsedto> addSupplier(@Valid @RequestBody SupplierRequestdto dto){
        return ResponseEntity.ok(supplierService.addSupplier(dto));
    }

    @GetMapping("/getallsuppliers")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<List<SupplierResponsedto>> getAllSuppliers(){
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    @PutMapping("/updatesupplier/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or (hasRole('SUPPLIER') and @securityService.isOwnSupplierId(authentication, #id))")
    public ResponseEntity<SupplierResponsedto> updateSupplier(@PathVariable Long id, @Valid @RequestBody SupplierRequestdto dto){
        return ResponseEntity.ok(supplierService.updateSupplier(id, dto));
    }

    @DeleteMapping("/deletesupplier/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id){
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getsupplierbyid/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF') or (hasRole('SUPPLIER') and @securityService.isOwnSupplierId(authentication, #id))")
    public ResponseEntity<SupplierResponsedto> getSupplierById(@PathVariable Long id){
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    @GetMapping("/getsuppliersbyproduct/{productname}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<List<SupplierResponsedto>> getSuppliersByProduct(@PathVariable String productname){
        return ResponseEntity.ok(supplierService.getSuppliersByProduct(productname));
    }

    @GetMapping("/getsuppliersbystatus/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<List<SupplierResponsedto>> getSuppliersByStatus(@PathVariable boolean status){
        return ResponseEntity.ok(supplierService.getSuppliersByStatus(status));
    }

    @GetMapping("/getsuppliersbyname/{name}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<List<SupplierResponsedto>> getSuppliersByName(@PathVariable String name){
        return ResponseEntity.ok(supplierService.getSuppliersByName(name));
    }

    @GetMapping("/category/{categoryName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<List<SupplierResponsedto>> getSuppliersByCategory(@PathVariable String categoryName) {
        return ResponseEntity.ok(supplierService.getSuppliersByCategory(categoryName));
    }

    @GetMapping("/my-products")
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<List<ProductResponsedto>> getMyProducts(Authentication authentication) {
        String email = authentication.getName();
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getSupplier() == null) {
            throw new ResourceNotFoundException("No supplier profile associated with this user");
        }
        return ResponseEntity.ok(productService.getProductsBySupplierId(user.getSupplier().getId()));
    }

    @GetMapping("/my-orders")
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<List<PurchaseOrderResponsedto>> getMyOrders(Authentication authentication) {
        String email = authentication.getName();
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getSupplier() == null) {
            throw new ResourceNotFoundException("No supplier profile associated with this user");
        }
        return ResponseEntity.ok(purchaseOrderService.findBySupplierId(user.getSupplier().getId()));
    }
}
