package com.example.indentory_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
public class CategoryRequestdto {
    @NotBlank(message = "Category name is required and must be unique")
    private String name;
    @NotBlank(message = "Description is required")
    private String description;
    @NotBlank(message = "Active status is required and must be either 'active' or 'inactive'")
    private String active_status;
    
}
