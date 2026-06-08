package com.example.indentory_management_system.dto;

import java.time.LocalDate;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class warehousesResponsedto {

    private Long id;
    private String name;
    private String warehouseCode;
    private Integer capacity;
    private String managerName;
    private String contactNumber;
    private String email;
    private String isActive;
    private LocalDate createdDate;
    private LocalDate updatedDate;
    private Long userId;
}
