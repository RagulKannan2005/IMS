package com.example.indentory_management_system.Security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.indentory_management_system.Entity.Users;
import com.example.indentory_management_system.Entity.Products;
import com.example.indentory_management_system.Entity.PurchaseOrder;
import com.example.indentory_management_system.Repository.UserRepository;
import com.example.indentory_management_system.Repository.ProductRepository;
import com.example.indentory_management_system.Repository.PurchaseOrderRepository;

import lombok.RequiredArgsConstructor;

@Service("securityService")
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    private Users getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return null;
        }
        String username = ((UserDetails) principal).getUsername();
        return userRepository.findByEmail(username).orElse(null);
    }

    public boolean isProductOwner(Authentication authentication, Long productId) {
        Users user = getAuthenticatedUser(authentication);
        if (user == null || user.getSupplier() == null) {
            return false;
        }
        Products product = productRepository.findById(productId).orElse(null);
        if (product == null || product.getSupplier() == null) {
            return false;
        }
        return product.getSupplier().getId().equals(user.getSupplier().getId());
    }

    public boolean isSupplierForOrder(Authentication authentication, Long orderId) {
        Users user = getAuthenticatedUser(authentication);
        if (user == null || user.getSupplier() == null) {
            return false;
        }
        PurchaseOrder order = purchaseOrderRepository.findById(orderId).orElse(null);
        if (order == null || order.getSupplier() == null) {
            return false;
        }
        return order.getSupplier().getId().equals(user.getSupplier().getId());
    }

    public boolean isOwnSupplierId(Authentication authentication, Long supplierId) {
        Users user = getAuthenticatedUser(authentication);
        if (user == null || user.getSupplier() == null) {
            return false;
        }
        return user.getSupplier().getId().equals(supplierId);
    }
}
