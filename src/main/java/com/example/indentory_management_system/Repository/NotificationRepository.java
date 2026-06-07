package com.example.indentory_management_system.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.indentory_management_system.Entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserEmailOrderByCreatedAtDesc(String email);
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
}
