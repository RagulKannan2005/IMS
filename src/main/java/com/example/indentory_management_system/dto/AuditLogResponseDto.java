package com.example.indentory_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponseDto {
    private String user;
    private String action;
    private String target;
    private String time;
    private String status;
}
