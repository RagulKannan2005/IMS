package com.example.indentory_management_system.ServiceImp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.indentory_management_system.Entity.Categories;
import com.example.indentory_management_system.Entity.Supplier;
import com.example.indentory_management_system.Entity.SupplierProduct;
import com.example.indentory_management_system.Entity.Users;
import com.example.indentory_management_system.Repository.CategoriesRepository;
import com.example.indentory_management_system.Repository.SupplierProductRepository;
import com.example.indentory_management_system.Repository.SupplierRepository;
import com.example.indentory_management_system.Repository.UserRepository;
import com.example.indentory_management_system.Service.SupplierProductService;
import com.example.indentory_management_system.dto.SupplierProductRequestdto;
import com.example.indentory_management_system.dto.SupplierProductResponsedto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierProductServiceImp implements SupplierProductService {

    private final SupplierProductRepository supplierProductRepository;
    private final CategoriesRepository categoriesRepository;
    private final UserRepository userRepository;
    private final SupplierRepository supplierRepository;

    private Supplier getCurrentSupplier() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users currentUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Current authenticated user not found"));

        if (!"SUPPLIER".equalsIgnoreCase(currentUser.getRole())) {
            throw new RuntimeException("Only suppliers can manage supplier products.");
        }

        Supplier supplier = currentUser.getSupplier();
        if (supplier == null) {
            supplier = Supplier.builder()
                .supplierName(currentUser.getFirstName() + " " + currentUser.getLastName())
                .contactPerson(currentUser.getFirstName() + " " + currentUser.getLastName())
                .supplier_email(currentUser.getEmail())
                .supplierPhone(currentUser.getPhone_number())
                .address("Not Provided")
                .status(true)
                .user(currentUser)
                .build();
            supplierRepository.save(supplier);
            
            currentUser.setSupplier(supplier);
            userRepository.save(currentUser);
        }
        return supplier;
    }

    @Override
    public SupplierProductResponsedto createSupplierProduct(SupplierProductRequestdto dto) {
        Supplier supplier = getCurrentSupplier();

        Categories category = categoriesRepository.findByName(dto.getCategory())
                .orElseThrow(() -> new RuntimeException("Category not found with name: " + dto.getCategory()));

        SupplierProduct product = SupplierProduct.builder()
                .sku(dto.getSku())
                .name(dto.getName())
                .description(dto.getDescription())
                .costPrice(dto.getCostPrice())
                .availableQuantity(dto.getAvailableQuantity())
                .isActive("active".equalsIgnoreCase(dto.getActive_status()))
                .categories(category)
                .supplier(supplier)
                .build();

        return mapToResponseDto(supplierProductRepository.save(product));
    }

    @Override
    public SupplierProductResponsedto updateSupplierProduct(Long id, SupplierProductRequestdto dto) {
        Supplier supplier = getCurrentSupplier();
        
        SupplierProduct product = supplierProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier Product not found with id: " + id));

        if (!product.getSupplier().getId().equals(supplier.getId())) {
            throw new RuntimeException("You do not have permission to update this product.");
        }

        Categories category = categoriesRepository.findByName(dto.getCategory())
                .orElseThrow(() -> new RuntimeException("Category not found with name: " + dto.getCategory()));

        product.setSku(dto.getSku());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setCostPrice(dto.getCostPrice());
        product.setAvailableQuantity(dto.getAvailableQuantity());
        product.setActive("active".equalsIgnoreCase(dto.getActive_status()));
        product.setCategories(category);

        return mapToResponseDto(supplierProductRepository.save(product));
    }

    @Override
    public SupplierProductResponsedto deleteSupplierProduct(Long id) {
        Supplier supplier = getCurrentSupplier();
        
        SupplierProduct product = supplierProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier Product not found with id: " + id));

        if (!product.getSupplier().getId().equals(supplier.getId())) {
            throw new RuntimeException("You do not have permission to delete this product.");
        }

        supplierProductRepository.delete(product);
        return mapToResponseDto(product);
    }

    @Override
    public SupplierProductResponsedto getSupplierProductById(Long id) {
        SupplierProduct product = supplierProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier Product not found with id: " + id));
        return mapToResponseDto(product);
    }

    @Override
    public List<SupplierProductResponsedto> getAllSupplierProducts() {
        return supplierProductRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SupplierProductResponsedto> getProductsBySupplierId(Long supplierId) {
        return supplierProductRepository.findBySupplierId(supplierId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private SupplierProductResponsedto mapToResponseDto(SupplierProduct product) {
        return SupplierProductResponsedto.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .description(product.getDescription())
                .costPrice(product.getCostPrice())
                .availableQuantity(product.getAvailableQuantity())
                .isActive(product.isActive())
                .category(product.getCategories() != null ? product.getCategories().getName() : null)
                .supplierId(product.getSupplier() != null ? product.getSupplier().getId() : null)
                .supplierName(product.getSupplier() != null ? product.getSupplier().getSupplierName() : null)
                .build();
    }
}
