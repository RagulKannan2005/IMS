package com.example.indentory_management_system.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.indentory_management_system.Service.ProductService;
import com.example.indentory_management_system.dto.ProductRequestdto;
import com.example.indentory_management_system.dto.ProductResponsedto;
import com.example.indentory_management_system.dto.StockAdjustmentRequest;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@RestController
public class ProductController {
    private final ProductService productservice;

    @PostMapping("/addproduct")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPPLIER')")
    public ProductResponsedto addproduct(@Valid @RequestBody ProductRequestdto dto){
        return productservice.createProduct(dto);
    }

    @GetMapping("/allproducts")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<List<ProductResponsedto>> getallproducts(){
        return ResponseEntity.ok().body(productservice.getAllProducts());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF') or (hasRole('SUPPLIER') and @securityService.isProductOwner(authentication, #id))")
    public ResponseEntity<ProductResponsedto> getbyid(@PathVariable Long id){
        return ResponseEntity.ok().body(productservice.getByProductId(id));
    }

    @GetMapping("/activeproducts")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<List<ProductResponsedto>> getactiveproducts(){
        return ResponseEntity.ok().body(productservice.getActiveProducts());
    }

    @GetMapping("/productname/{productname}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<ProductResponsedto> getbyproductname(@PathVariable String productname){
        return ResponseEntity.ok().body(productservice.getByProductName(productname));
    }

    @GetMapping("/productsku/{sku}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<ProductResponsedto> getbyproductsku(@PathVariable String sku){
        return ResponseEntity.ok().body(productservice.getByProductSku(sku));
    }

    @GetMapping("/productcategory/{productcategory}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<List<ProductResponsedto>> getbyproductcategory(@PathVariable String productcategory){
        return ResponseEntity.ok(productservice.findproductCategory(productcategory));
    }
    
    @PutMapping("/updateproduct/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or (hasRole('SUPPLIER') and @securityService.isProductOwner(authentication, #id))")
    public ResponseEntity<ProductResponsedto> updateproduct(@PathVariable Long id, @Valid @RequestBody ProductRequestdto dto){
        return ResponseEntity.ok().body(productservice.updateProduct(id, dto));
    }

    @DeleteMapping("/deleteproduct/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or (hasRole('SUPPLIER') and @securityService.isProductOwner(authentication, #id))")
    public ResponseEntity<ProductResponsedto> deleteproduct(@PathVariable Long id){
        return ResponseEntity.ok().body(productservice.deleteProduct(id));
    }

    @PostMapping("/{id}/adjust-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ProductResponsedto> adjustStock(
            @PathVariable Long id, 
            @Valid @RequestBody StockAdjustmentRequest request) {
        return ResponseEntity.ok().body(productservice.adjustStock(id, request));
    }

    @GetMapping("/supplier/{supplierId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF') or (hasRole('SUPPLIER') and @securityService.isOwnSupplierId(authentication, #supplierId))")
    public ResponseEntity<List<ProductResponsedto>> getProductsBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(productservice.getProductsBySupplierId(supplierId));
    }
}
