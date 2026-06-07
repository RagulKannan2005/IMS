package com.example.indentory_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMeResponseDto {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String firstName;
    private String lastName;
}
