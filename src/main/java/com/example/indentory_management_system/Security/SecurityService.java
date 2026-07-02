package com.example.indentory_management_system.Security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.indentory_management_system.Entity.Users;
import com.example.indentory_management_system.Entity.Products;
import com.example.indentory_management_system.Entity.PurchaseOrder;
import com.example.indentory_management_system.Entity.Supplier;
import com.example.indentory_management_system.Repository.UserRepository;
import com.example.indentory_management_system.Repository.ProductRepository;
import com.example.indentory_management_system.Repository.PurchaseOrderRepository;
import com.example.indentory_management_system.Repository.SupplierRepository;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Service("securityService")
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierRepository supplierRepository;

    private Users getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return null;
        }
        String email = ((UserDetails) principal).getUsername();
        return userRepository.findByEmail(email).orElse(null);
    }

    private Supplier getSupplierForUser(Users user) {
        if (user == null) return null;
        List<Supplier> suppliers = supplierRepository.findByUserId(user.getId());
        return suppliers.isEmpty() ? null : suppliers.get(0);
    }

    public boolean isSupplierForOrder(Authentication authentication, Long orderId) {
        Users user = getAuthenticatedUser(authentication);
        Supplier supplier = getSupplierForUser(user);
        if (supplier == null) {
            return false;
        }
        PurchaseOrder order = purchaseOrderRepository.findById(orderId).orElse(null);
        if (order == null || order.getSupplier() == null) {
            return false;
        }
        return order.getSupplier().getId().equals(supplier.getId());
    }

    public boolean isOwnSupplierId(Authentication authentication, Long supplierId) {
        Users user = getAuthenticatedUser(authentication);
        Supplier supplier = getSupplierForUser(user);
        if (supplier == null) {
            return false;
        }
        return supplier.getId().equals(supplierId);
    }
}
