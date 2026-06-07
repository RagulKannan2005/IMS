package com.example.indentory_management_system.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.indentory_management_system.Entity.DailyTask;
import com.example.indentory_management_system.Repository.DailyTaskRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class DailyTaskController {

    private final DailyTaskRepository dailyTaskRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('STAFF', 'MANAGER', 'ADMIN')")
    public ResponseEntity<List<DailyTask>> getAllTasks() {
        return ResponseEntity.ok(dailyTaskRepository.findAll());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('STAFF', 'MANAGER', 'ADMIN')")
    public ResponseEntity<DailyTask> createTask(@RequestBody DailyTask task) {
        return ResponseEntity.ok(dailyTaskRepository.save(task));
    }

    @PutMapping("/{id}/toggle")
    @PreAuthorize("hasAnyRole('STAFF', 'MANAGER', 'ADMIN')")
    public ResponseEntity<DailyTask> toggleTask(@PathVariable Long id) {
        return dailyTaskRepository.findById(id).map(task -> {
            task.setDone(!task.isDone());
            return ResponseEntity.ok(dailyTaskRepository.save(task));
        }).orElse(ResponseEntity.notFound().build());
    }
}
