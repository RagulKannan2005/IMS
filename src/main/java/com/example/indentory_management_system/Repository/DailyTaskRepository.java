package com.example.indentory_management_system.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.indentory_management_system.Entity.DailyTask;

@Repository
public interface DailyTaskRepository extends JpaRepository<DailyTask, Long> {
}
