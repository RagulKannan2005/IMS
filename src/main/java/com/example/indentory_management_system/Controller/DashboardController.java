package com.example.indentory_management_system.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.indentory_management_system.Service.DashboardService;
import com.example.indentory_management_system.dto.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/dashboards")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDashboardDto> getAdminDashboard() {
        return ResponseEntity.ok(dashboardService.getAdminDashboard());
    }

    @GetMapping("/manager")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ManagerDashboardDto> getManagerDashboard() {
        return ResponseEntity.ok(dashboardService.getManagerDashboard());
    }

    @GetMapping("/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<StaffDashboardDto> getStaffDashboard() {
        return ResponseEntity.ok(dashboardService.getStaffDashboard());
    }

    @GetMapping("/supplier")
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<SupplierDashboardDto> getSupplierDashboard(Authentication authentication) {
        String email = authentication.getName(); // principal name contains email
        return ResponseEntity.ok(dashboardService.getSupplierDashboard(email));
    }
}
