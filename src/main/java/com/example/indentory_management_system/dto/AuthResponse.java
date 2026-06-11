package com.example.indentory_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private String role;
    private Long supplierId;
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
