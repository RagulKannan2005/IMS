package com.example.indentory_management_system.ServiceImp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.indentory_management_system.Entity.Supplier;
import com.example.indentory_management_system.Entity.Users;
import com.example.indentory_management_system.Exception.ResourceNotFoundException;
import com.example.indentory_management_system.Repository.SupplierRepository;
import com.example.indentory_management_system.Repository.UserRepository;
import com.example.indentory_management_system.Service.UserService;
import com.example.indentory_management_system.dto.UserRequestdto;
import com.example.indentory_management_system.dto.UserResponsedto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userrepo;
    private final SupplierRepository supplierrepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponsedto createUser(UserRequestdto dto) {
        Supplier supplier = null;
        if (dto.getSupplierId() != null) {
            supplier = supplierrepo.findById(dto.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + dto.getSupplierId()));
        }

        String createdBy = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();

        Users user = Users.builder()
                .username(dto.getUsername())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phone_number(dto.getPhone_number())
                .role(dto.getRole())
                .createdBy(createdBy)
                .build();
        Users saved = userrepo.save(user);

        if (supplier != null) {
            supplier.setUser(saved);
            supplierrepo.save(supplier);
            saved.setSupplier(supplier);
        }

        return toDto(saved);
    }

    @Override
    public List<UserResponsedto> getUserAllusers() {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = (auth != null) ? auth.getName() : null;
        if (currentUsername == null) {
            return java.util.Collections.emptyList();
        }

        return userrepo.findAll()
                .stream()
                .filter(user -> {
                    // Show users created by the current user
                    if (currentUsername.equals(user.getCreatedBy())) {
                        return true;
                    }
                    // Show the user itself
                    if (currentUsername.equals(user.getUsername())) {
                        return true;
                    }
                    // For the default "admin" user, also show system-seeded or legacy users (createdBy is null, empty, or anonymousUser)
                    if ("admin".equals(currentUsername) && 
                            (user.getCreatedBy() == null || 
                             user.getCreatedBy().trim().isEmpty() || 
                             "anonymousUser".equals(user.getCreatedBy()))) {
                        return true;
                    }
                    return false;
                })
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponsedto updateuser(Long id, UserRequestdto userRequestdto) {
        Users user = userrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        Supplier supplier = null;
        if (userRequestdto.getSupplierId() != null) {
            supplier = supplierrepo.findById(userRequestdto.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + userRequestdto.getSupplierId()));
        }

        if (user.getSupplier() != null && !user.getSupplier().equals(supplier)) {
            Supplier oldSupplier = user.getSupplier();
            oldSupplier.setUser(null);
            supplierrepo.save(oldSupplier);
        }

        user.setUsername(userRequestdto.getUsername());
        user.setFirstName(userRequestdto.getFirstName());
        user.setLastName(userRequestdto.getLastName());
        user.setEmail(userRequestdto.getEmail());
        user.setPhone_number(userRequestdto.getPhone_number());
        user.setRole(userRequestdto.getRole());

        if (userRequestdto.getPassword() != null && !userRequestdto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequestdto.getPassword()));
        }

        Users saved = userrepo.save(user);

        if (supplier != null) {
            supplier.setUser(saved);
            supplierrepo.save(supplier);
            saved.setSupplier(supplier);
        } else {
            saved.setSupplier(null);
        }

        return toDto(saved);
    }

    @Override
    public UserResponsedto deleteuser(Long id) {
        Users user = userrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userrepo.delete(user);
        return toDto(user);
    }

    @Override
    public UserResponsedto getbyUsername(String username) {
        Users user = userrepo.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + username));
        return toDto(user);
    }

    private UserResponsedto toDto(Users s) {
        return UserResponsedto.builder()
                .id(s.getId())
                .username(s.getUsername())
                .firstName(s.getFirstName())
                .lastName(s.getLastName())
                .password(s.getPassword())
                .email(s.getEmail())
                .phone_number(s.getPhone_number())
                .role(s.getRole())
                .createdBy(s.getCreatedBy())
                .supplierId(s.getSupplier() != null ? s.getSupplier().getId() : null)
                .build();
    }
}
