package com.example.indentory_management_system.ServiceImp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
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

    private Users getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        Object principal = auth.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return null;
        }
        String email = ((UserDetails) principal).getUsername();
        return userrepo.findByEmail(email).orElse(null);
    }

    @Override
    public UserResponsedto createUser(UserRequestdto dto) {
        Users creator = getAuthenticatedUser();
        
        // Enforce role boundary: MANAGERS can only create STAFF
        if (creator != null && "MANAGER".equalsIgnoreCase(creator.getRole())) {
            if (!"STAFF".equalsIgnoreCase(dto.getRole())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Managers are only allowed to create Staff accounts.");
            }
        }

        Supplier supplier = null;
        if (dto.getSupplierId() != null) {
            supplier = supplierrepo.findById(dto.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + dto.getSupplierId()));
        }

        Users user = Users.builder()
                .username(dto.getUsername())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phone_number(dto.getPhoneNumber())
                .role(dto.getRole().toUpperCase())
                .createdBy(creator)
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
        Users actor = getAuthenticatedUser();
        if (actor == null) {
            return List.of();
        }

        if ("MANAGER".equalsIgnoreCase(actor.getRole())) {
            // Managers only see staff created by themselves
            return userrepo.findByCreatedById(actor.getId())
                    .stream().map(this::toDto).collect(Collectors.toList());
        }

        // Admins see all users
        return userrepo.findAll()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public UserResponsedto updateuser(Long id, UserRequestdto userRequestdto) {
        Users actor = getAuthenticatedUser();
        Users user = userrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Enforce update boundaries for Managers
        if (actor != null && "MANAGER".equalsIgnoreCase(actor.getRole())) {
            if (user.getCreatedBy() == null || !user.getCreatedBy().getId().equals(actor.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to modify this user.");
            }
            if (!"STAFF".equalsIgnoreCase(userRequestdto.getRole())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Managers are only allowed to assign the STAFF role.");
            }
        }

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
        user.setPhone_number(userRequestdto.getPhoneNumber());
        user.setRole(userRequestdto.getRole().toUpperCase());

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
        Users actor = getAuthenticatedUser();
        Users user = userrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Enforce delete boundaries for Managers
        if (actor != null && "MANAGER".equalsIgnoreCase(actor.getRole())) {
            if (user.getCreatedBy() == null || !user.getCreatedBy().getId().equals(actor.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete this user.");
            }
        }

        userrepo.delete(user);
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
                .phoneNumber(s.getPhone_number())
                .role(s.getRole())
                .supplierId(s.getSupplier() != null ? s.getSupplier().getId() : null)
                .createdById(s.getCreatedBy() != null ? s.getCreatedBy().getId() : null)
                .createdByUsername(s.getCreatedBy() != null ? s.getCreatedBy().getUsername() : null)
                .build();
    }

    @Override
    public com.example.indentory_management_system.Entity.Users findByEmail(String email) {
        return userrepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}
