package com.example.indentory_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class warehousesRequestdto {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Warehouse code is required and must be unique")
    private String warehouseCode;
    @NotNull(message = "Capacity is required")
    private Integer capacity;
    @NotBlank(message = "Manager name is required")
    private String managerName;
    @NotBlank(message = "Contact number is required")
    private String contactNumber;
    @NotBlank(message = "Email is required and must be unique")
    private String email;
    
    private Long userId;
    
    
}
