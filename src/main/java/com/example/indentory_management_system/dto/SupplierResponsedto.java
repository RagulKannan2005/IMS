package com.example.indentory_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierResponsedto {

    private Long id;
    private String supplierName;
    private String contactPerson;
    private String supplier_email;
    private String supplierPhone;
    private String address;
    private String status;

}
