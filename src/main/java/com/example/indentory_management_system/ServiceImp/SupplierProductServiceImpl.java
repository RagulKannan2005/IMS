package com.example.indentory_management_system.ServiceImp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.indentory_management_system.Entity.SupplierProduct;
import com.example.indentory_management_system.Repository.SupplierProductRepository;
import com.example.indentory_management_system.Service.SupplierProductService;
import com.example.indentory_management_system.dto.SupplierProductDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierProductServiceImpl implements SupplierProductService {

    private final SupplierProductRepository supplierProductRepository;

    @Override
    public List<SupplierProductDto> getSupplierProductsBySupplierId(Long supplierId) {
        return supplierProductRepository.findBySupplierId(supplierId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private SupplierProductDto toDto(SupplierProduct sp) {
        return SupplierProductDto.builder()
                .id(sp.getId())
                .supplierId(sp.getSupplier().getId())
                .productId(sp.getProduct().getId())
                .productName(sp.getProduct().getName())
                .productSku(sp.getProduct().getSku())
                .availableQuantity(sp.getAvailableQuantity())
                .unitPrice(sp.getUnitPrice())
                .build();
    }
}
