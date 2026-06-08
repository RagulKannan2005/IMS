package com.example.indentory_management_system.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.indentory_management_system.Service.CategoryService;
import com.example.indentory_management_system.dto.*;

import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/newcategory")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public CategoryResponsedto createCategory(@Valid @RequestBody CategoryRequestdto categoryRequestdto) {
        return categoryService.createCategory(categoryRequestdto);
    }

    @GetMapping("/allcategories")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF', 'SUPPLIER')")
    public List<CategoryResponsedto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/activecategories")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF', 'SUPPLIER')")
    public List<CategoryResponsedto> getActiveCategories() {
        return categoryService.getActiveCategories();
    }

    @GetMapping("/categorydescription/{description}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF', 'SUPPLIER')")
    public CategoryResponsedto getbycategorydescription(@PathVariable String description) {
        return categoryService.getbycategorydescription(description);
    }

    @PutMapping("/updatecategory/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public CategoryResponsedto updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequestdto categoryRequestdto) {
        return categoryService.updateCategory(id, categoryRequestdto);
    }

    @DeleteMapping("/deletecategory/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public CategoryResponsedto deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }
    
}
