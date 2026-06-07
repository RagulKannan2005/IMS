package com.example.indentory_management_system.Service;

import com.example.indentory_management_system.dto.*;

public interface DashboardService {
    AdminDashboardDto getAdminDashboard();
    ManagerDashboardDto getManagerDashboard();
    StaffDashboardDto getStaffDashboard();
    SupplierDashboardDto getSupplierDashboard(String email);
}
