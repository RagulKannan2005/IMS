package com.example.indentory_management_system.ServiceImp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.indentory_management_system.Entity.Categories;
import com.example.indentory_management_system.Entity.Products;
import com.example.indentory_management_system.Repository.CategoriesRepository;
import com.example.indentory_management_system.Repository.ProductRepository;
import com.example.indentory_management_system.Repository.UserRepository;
import com.example.indentory_management_system.Service.ProductService;
import com.example.indentory_management_system.dto.PriceUpdateRequestdto;
import com.example.indentory_management_system.dto.ProductRequestdto;
import com.example.indentory_management_system.dto.ProductResponsedto;
import com.example.indentory_management_system.dto.StockAdjustmentRequest;
import com.example.indentory_management_system.Entity.Users;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

    private final ProductRepository productrepo;
    private final CategoriesRepository categoriesRepository;
    private final UserRepository userRepository;

    @Override
    public ProductResponsedto createProduct(ProductRequestdto dto) {
        Categories category = categoriesRepository.findByName(dto.getCategory())
                .orElseThrow(() -> new RuntimeException("Category not found with name: " + dto.getCategory()));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Current authenticated user not found"));

        Products products = Products.builder()
                .sku(dto.getSku())
                .name(dto.getName())
                .description(dto.getDescription())
                .costPrice(dto.getCostPrice())
                .sellingPrice(dto.getSellingPrice())
                .stockQuantity(dto.getStockQuantity())
                .reorderLevel(dto.getReorderLevel())
                .reorderQuantity(dto.getReorderQuantity())
                .isActive("active".equalsIgnoreCase(dto.getActive_status()))
                .categories(category)
                .user(currentUser)
                .build();

        Products savedProduct = productrepo.save(products);
        return mapToResponseDto(savedProduct);
    }

    @Override
    public List<ProductResponsedto> getAllProducts() {
        return productrepo.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponsedto> getActiveProducts() {
        return productrepo.findAll().stream()
                .filter(Products::isActive)
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponsedto getByProductId(Long id) {
        Products product = productrepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return mapToResponseDto(product);
    }

    @Override
    public ProductResponsedto getByProductSku(String sku) {
        Products product = productrepo.findBySku(sku)
                .orElseThrow(() -> new RuntimeException("Product not found with SKU: " + sku));
        return mapToResponseDto(product);
    }

    @Override
    public ProductResponsedto getByProductName(String name) {
        Products product = productrepo.findByName(name)
                .orElseThrow(() -> new RuntimeException("Product not found with name: " + name));
        return mapToResponseDto(product);
    }

    @Override
    public ProductResponsedto updateProduct(Long id, ProductRequestdto dto) {
        Products product = productrepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        Categories category = categoriesRepository.findByName(dto.getCategory())
                .orElseThrow(() -> new RuntimeException("Category not found with name: " + dto.getCategory()));

        product.setSku(dto.getSku());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setCostPrice(dto.getCostPrice());
        product.setSellingPrice(dto.getSellingPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setReorderLevel(dto.getReorderLevel());
        product.setReorderQuantity(dto.getReorderQuantity());
        product.setActive("active".equalsIgnoreCase(dto.getActive_status()));
        product.setCategories(category);

        Products updatedProduct = productrepo.save(product);
        return mapToResponseDto(updatedProduct);
    }

    @Override
    public ProductResponsedto deleteProduct(Long id) {
        Products product = productrepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        productrepo.delete(product);
        return mapToResponseDto(product);
    }

    @Override
    public List<ProductResponsedto> findproductCategory(String name) {
        List<Products> products = productrepo.findByCategoryName(name);
        if (products.isEmpty()) {
            throw new RuntimeException("No products found in category: " + name);
        }
        return products.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponsedto adjustStock(Long id, StockAdjustmentRequest request) {
        Products product = productrepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        int newStock = product.getStockQuantity() + request.getQuantityChange();
        if (newStock < 0) {
            throw new RuntimeException("Insufficient stock. Current stock: " + product.getStockQuantity());
        }

        product.setStockQuantity(newStock);
        Products updatedProduct = productrepo.save(product);
        return mapToResponseDto(updatedProduct);
    }

    @Override
    public ProductResponsedto updateProductPrice(Long id, PriceUpdateRequestdto request) {
        Products product = productrepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setSellingPrice(request.getSellingPrice());
        product.setCostPrice(request.getCostPrice());

        Products updatedProduct = productrepo.save(product);
        return mapToResponseDto(updatedProduct);
    }

    private ProductResponsedto mapToResponseDto(Products product) {
        return ProductResponsedto.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .description(product.getDescription())
                .costPrice(product.getCostPrice())
                .sellingPrice(product.getSellingPrice())
                .stockQuantity(product.getStockQuantity())
                .reorderLevel(product.getReorderLevel())
                .reorderQuantity(product.getReorderQuantity())
                .isActive(product.isActive())
                .build();
    }
}
