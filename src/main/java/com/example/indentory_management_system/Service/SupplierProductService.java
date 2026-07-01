package com.example.indentory_management_system.Service;

import java.util.List;
import com.example.indentory_management_system.dto.SupplierProductRequestdto;
import com.example.indentory_management_system.dto.SupplierProductResponsedto;

public interface SupplierProductService {
    SupplierProductResponsedto createSupplierProduct(SupplierProductRequestdto dto);
    SupplierProductResponsedto updateSupplierProduct(Long id, SupplierProductRequestdto dto);
    SupplierProductResponsedto deleteSupplierProduct(Long id);
    SupplierProductResponsedto getSupplierProductById(Long id);
    List<SupplierProductResponsedto> getAllSupplierProducts();
    List<SupplierProductResponsedto> getProductsBySupplierId(Long supplierId);
}
