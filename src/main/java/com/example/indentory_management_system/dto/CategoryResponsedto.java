package com.example.indentory_management_system.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponsedto {
    
    private Long id;
    private String name;
    private String description;
    private String active_status;
}
