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

import jakarta.transaction.Transactional;
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
    @Transactional 
    public UserResponsedto createUser(UserRequestdto dto) {
        Users creator = getAuthenticatedUser();
        
        // Enforce role boundary: MANAGERS can only create STAFF
        if (creator != null && "MANAGER".equalsIgnoreCase(creator.getRole())) {
            if (!"STAFF".equalsIgnoreCase(dto.getRole())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Managers are only allowed to create Staff accounts.");
            }
        }

        if (userrepo.findByUsername(dto.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username '" + dto.getUsername() + "' is already taken.");
        }
        if (userrepo.findByEmail(dto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email '" + dto.getEmail() + "' is already registered.");
        }

        Supplier supplier = null;
        if (dto.getSupplierId() != null) {
            supplier = supplierrepo.findById(dto.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + dto.getSupplierId()));
            if (supplier.getUser() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This supplier profile is already associated with another user.");
            }
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
            userrepo.save(saved);
        } else if ("SUPPLIER".equalsIgnoreCase(dto.getRole())) {
            for (Supplier s : supplierrepo.findAll()) {
                if (s.getSupplier_email() != null && s.getSupplier_email().equalsIgnoreCase(dto.getEmail())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A supplier with email '" + dto.getEmail() + "' already exists.");
                }
                if (s.getSupplierPhone() != null && s.getSupplierPhone().equalsIgnoreCase(dto.getPhoneNumber())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A supplier with phone number '" + dto.getPhoneNumber() + "' already exists.");
                }
            }

            String company = (dto.getCompanyName() != null && !dto.getCompanyName().isBlank())
                    ? dto.getCompanyName().trim()
                    : (dto.getFirstName() + " " + dto.getLastName());
            String addr = (dto.getAddress() != null && !dto.getAddress().isBlank())
                    ? dto.getAddress().trim()
                    : "Address Not Provided";

            Supplier newSupplier = Supplier.builder()
                    .supplierName(company)
                    .contactPerson(dto.getFirstName() + " " + dto.getLastName())
                    .supplier_email(dto.getEmail())
                    .supplierPhone(dto.getPhoneNumber())
                    .address(addr)
                    .status(true)
                    .user(saved)
                    .build();
            supplierrepo.save(newSupplier);
            saved.setSupplier(newSupplier);
            userrepo.save(saved);
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

        if ("ADMIN".equalsIgnoreCase(actor.getRole())) {
            // Root/Bootstrap admin has global visibility
            if ("admin".equalsIgnoreCase(actor.getUsername()) || "admin@ims.com".equalsIgnoreCase(actor.getEmail())) {
                return userrepo.findAll()
                        .stream().map(this::toDto).collect(Collectors.toList());
            }

            // Other admins only see users created by themselves or by managers under their administration
            List<Users> myDirectUsers = userrepo.findByCreatedById(actor.getId());
            List<Long> managerIds = myDirectUsers.stream()
                    .filter(u -> "MANAGER".equalsIgnoreCase(u.getRole()))
                    .map(Users::getId)
                    .collect(Collectors.toList());

            List<Users> allMyTeam = new java.util.ArrayList<>(myDirectUsers);
            for (Long mId : managerIds) {
                allMyTeam.addAll(userrepo.findByCreatedById(mId));
            }

            return allMyTeam.stream().distinct().map(this::toDto).collect(Collectors.toList());
        }

        return List.of();
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
            if (supplier.getUser() != null && !supplier.getUser().getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This supplier profile is already associated with another user.");
            }
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
