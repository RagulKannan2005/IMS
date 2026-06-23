package com.example.indentory_management_system.ServiceImp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.indentory_management_system.Entity.Supplier;
import com.example.indentory_management_system.Entity.Users;
import com.example.indentory_management_system.Exception.ResourceNotFoundException;
import com.example.indentory_management_system.Repository.SupplierRepository;
import com.example.indentory_management_system.Repository.UserRepository;
import com.example.indentory_management_system.Service.SupplierService;
import com.example.indentory_management_system.dto.SupplierRequestdto;
import com.example.indentory_management_system.dto.SupplierResponsedto;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierServiceImp implements SupplierService {

    private final SupplierRepository supplierrepo;
    private final UserRepository userRepository;

    @Override
    public SupplierResponsedto addSupplier(SupplierRequestdto dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users currentUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Current authenticated user not found"));

        if (currentUser.getSupplier() != null) {
            throw new RuntimeException("A Supplier profile already exists for this user.");
        }

        Supplier supplier = Supplier.builder()
                .supplierName(dto.getSupplierName())
                .contactPerson(dto.getContactPerson())
                .supplier_email(dto.getSupplier_email())
                .supplierPhone(dto.getSupplierPhone())
                .address(dto.getAddress())
                .status(dto.isStatus())
                .user(currentUser)
                .build();
        supplierrepo.save(supplier);
        return toDto(supplier);
    }

    @Override
    public SupplierResponsedto updateSupplier(Long id, SupplierRequestdto dto) {
        Supplier supplier = supplierrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        // Do not allow changing the linked user ID during an update.
        // The user ID was locked to the supplier upon creation.

        supplier.setSupplierName(dto.getSupplierName());
        supplier.setContactPerson(dto.getContactPerson());
        supplier.setSupplier_email(dto.getSupplier_email());
        supplier.setSupplierPhone(dto.getSupplierPhone());
        supplier.setAddress(dto.getAddress());
        supplier.setStatus(dto.isStatus());

        supplierrepo.save(supplier);
        return toDto(supplier);
    }

    @Override
    public void deleteSupplier(Long id) {
        Supplier supplier = supplierrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
        supplierrepo.delete(supplier);
    }

    @Override
    public SupplierResponsedto getSupplierById(Long id) {
        Supplier supplier = supplierrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
        return toDto(supplier);
    }

    @Override
    public List<SupplierResponsedto> getAllSuppliers() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users currentUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Current authenticated user not found"));

        List<Supplier> suppliers = supplierrepo.findByUserId(currentUser.getId());

        return suppliers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SupplierResponsedto> getSuppliersByProduct(String productname){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users currentUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Current authenticated user not found"));

        List<Supplier> suppliers = supplierrepo.findSuppliersByProductNameAndUserId(productname, currentUser.getId());

        return suppliers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<SupplierResponsedto> searchSuppliers(String keyword){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users currentUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Current authenticated user not found"));

        List<Supplier> suppliers = supplierrepo.searchSuppliersByUserId(keyword, currentUser.getId());

        return suppliers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SupplierResponsedto> getSuppliersByStatus(boolean status){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users currentUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Current authenticated user not found"));

        List<Supplier> suppliers = supplierrepo.findSuppliersByStatusAndUserId(status, currentUser.getId());

        return suppliers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SupplierResponsedto> getSuppliersByName(String name){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users currentUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Current authenticated user not found"));

        List<Supplier> suppliers = supplierrepo.findSuppliersByNameAndUserId(name, currentUser.getId());

        return suppliers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private SupplierResponsedto toDto(Supplier s) {
        return SupplierResponsedto.builder()
                .id(s.getId())
                .supplierName(s.getSupplierName())
                .contactPerson(s.getContactPerson())
                .supplier_email(s.getSupplier_email())
                .supplierPhone(s.getSupplierPhone())
                .address(s.getAddress())
                .status(s.isStatus())
                .userId(s.getUser() != null ? s.getUser().getId() : null)
                .build();
    }
}
