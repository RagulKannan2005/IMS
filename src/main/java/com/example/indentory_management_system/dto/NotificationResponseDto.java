package com.example.indentory_management_system.dto;

import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
    private Long id;
    private Long userId;
    private String userEmail;
    private String title;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;
}
