package com.example.indentory_management_system.Service;

import java.util.List;

import com.example.indentory_management_system.dto.SupplierRequestdto;
import com.example.indentory_management_system.dto.SupplierResponsedto;

public interface SupplierService {

    public SupplierResponsedto addSupplier(SupplierRequestdto dto);

    public SupplierResponsedto updateSupplier(Long id, SupplierRequestdto dto);

    public void deleteSupplier(Long id);

    public SupplierResponsedto getSupplierById(Long id);

    public List<SupplierResponsedto> getAllSuppliers();

    public List<SupplierResponsedto> searchSuppliers(String keyword);

    public List<SupplierResponsedto> getSuppliersByStatus(String status);

    List<SupplierResponsedto> getSuppliersByProduct(String productName);

    List<SupplierResponsedto> getSuppliersByName(String name);

}
