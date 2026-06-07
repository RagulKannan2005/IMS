package com.example.indentory_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponsedto {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String phoneNumber;
    private String role;
    private Long supplierId;
    private Long createdById;
    private String createdByUsername;
}
