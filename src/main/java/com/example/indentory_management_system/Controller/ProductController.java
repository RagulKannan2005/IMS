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
    public ProductResponsedto addproduct(@Valid @RequestBody ProductRequestdto dto){
        return productservice.createProduct(dto);
    }

    @GetMapping("/allproducts")
    public ResponseEntity<List<ProductResponsedto>> getallproducts(){
        return ResponseEntity.ok().body(productservice.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponsedto> getbyid(@PathVariable Long id){
        return ResponseEntity.ok().body(productservice.getByProductId(id));
    }

    @GetMapping("/activeproducts")
    public ResponseEntity<List<ProductResponsedto>> getactiveproducts(){
        return ResponseEntity.ok().body(productservice.getActiveProducts());
    }

    @GetMapping("/productname/{productname}")
    public ResponseEntity<ProductResponsedto> getbyproductname(@PathVariable String productname){
        return ResponseEntity.ok().body(productservice.getByProductName(productname));
    }

    @GetMapping("/productsku/{sku}")
    public ResponseEntity<ProductResponsedto> getbyproductsku(@PathVariable String sku){
        return ResponseEntity.ok().body(productservice.getByProductSku(sku));
    }

    @GetMapping("/productcategory/{productcategory}")
    public ResponseEntity<List<ProductResponsedto>> getbyproductcategory(@PathVariable String productcategory){
        return ResponseEntity.ok(productservice.findproductCategory(productcategory));
    }
    
    @PutMapping("/updateproduct/{id}")
    public ResponseEntity<ProductResponsedto> updateproduct(@PathVariable Long id, @Valid @RequestBody ProductRequestdto dto){
        return ResponseEntity.ok().body(productservice.updateProduct(id, dto));
    }

    @DeleteMapping("/deleteproduct/{id}")
    public ResponseEntity<ProductResponsedto> deleteproduct(@PathVariable Long id){
        return ResponseEntity.ok().body(productservice.deleteProduct(id));
    }

    @PostMapping("/{id}/adjust-stock")
    public ResponseEntity<ProductResponsedto> adjustStock(
            @PathVariable Long id, 
            @Valid @RequestBody StockAdjustmentRequest request) {
        return ResponseEntity.ok().body(productservice.adjustStock(id, request));
    }
}
