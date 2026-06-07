package com.example.indentory_management_system.Service;

import java.util.List;
import com.example.indentory_management_system.dto.NotificationResponseDto;

public interface NotificationService {
    List<NotificationResponseDto> getNotificationsForUser(String email);
    void markAsRead(Long id);
    void markAllAsRead(String email);
    void createNotification(String email, String title, String message);
}
