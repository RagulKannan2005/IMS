package com.example.indentory_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SupplierRequestdto {

    @NotBlank(message = "suppliername is required")
    private String supplierName;

    @NotBlank(message = "contactperson is required")
    private String contactPerson;

    @NotBlank(message = "supplier_email is required")
    private String supplierEmail;

    @NotBlank(message = "supplierPhone is required")
    private String supplierPhone;

    @NotBlank(message = "address is required")
    private String address;

    private boolean status;

    private Long userId;
}
