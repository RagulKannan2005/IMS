package com.example.indentory_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequestdto {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required and must be unique and valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Phone number is required")
    private String phone_number;

    @NotBlank(message = "Role is required and must be either 'ADMIN', 'MANAGER', 'STAFF', or 'SUPPLIER'")
    private String role;

    private Long supplierId;

}
