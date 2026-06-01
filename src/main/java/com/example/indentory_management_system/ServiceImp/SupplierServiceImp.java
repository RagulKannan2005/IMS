package com.example.indentory_management_system.ServiceImp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.indentory_management_system.Entity.Supplier;
import com.example.indentory_management_system.Exception.ResourceNotFoundException;
import com.example.indentory_management_system.Repository.SupplierRepository;
import com.example.indentory_management_system.Service.SupplierService;
import com.example.indentory_management_system.dto.SupplierRequestdto;
import com.example.indentory_management_system.dto.SupplierResponsedto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierServiceImp implements SupplierService {

    private final SupplierRepository supplierrepo;

    @Override
    public SupplierResponsedto addSupplier(SupplierRequestdto dto) {
        Supplier supplier = Supplier.builder()
                .supplierName(dto.getSupplierName())
                .contactPerson(dto.getContactPerson())
                .supplier_email(dto.getSupplier_email())
                .supplierPhone(dto.getSupplierPhone())
                .address(dto.getAddress())
                .status(dto.getStatus())
                .build();
        supplierrepo.save(supplier);
        return toDto(supplier);
    }

    @Override
    public SupplierResponsedto updateSupplier(Long id, SupplierRequestdto dto) {
        Supplier supplier = supplierrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        supplier.setSupplierName(dto.getSupplierName());
        supplier.setContactPerson(dto.getContactPerson());
        supplier.setSupplier_email(dto.getSupplier_email());
        supplier.setSupplierPhone(dto.getSupplierPhone());
        supplier.setAddress(dto.getAddress());
        supplier.setStatus(dto.getStatus());

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
        return supplierrepo.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SupplierResponsedto> getSuppliersByProduct(String productname){
        List<Supplier> suppliers = supplierrepo.findSuppliersByProductName(productname);
        return suppliers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<SupplierResponsedto> searchSuppliers(String keyword){
        List<Supplier> suppliers = supplierrepo.searchSuppliers(keyword);
        return suppliers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SupplierResponsedto> getSuppliersByStatus(String status){
        List<Supplier> suppliers = supplierrepo.findSuppliersByStatus(status);
        return suppliers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SupplierResponsedto> getSuppliersByName(String name){
        List<Supplier> suppliers = supplierrepo.findSuppliersByName(name);
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
                .status(s.getStatus())
                .build();
    }
}
