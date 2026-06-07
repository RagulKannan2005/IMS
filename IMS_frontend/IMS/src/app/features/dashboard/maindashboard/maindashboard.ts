import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';
import { AdminDashboard } from '../admin-dashboard/admin-dashboard';
import { ManagerDashboard } from '../manager-dashboard/manager-dashboard';
import { StaffDashboard } from '../staff-dashboard/staff-dashboard';
import { SupplierDashboard } from '../supplier-dashboard/supplier-dashboard';

@Component({
  selector: 'app-maindashboard',
  standalone: true,
  imports: [
    CommonModule,
    AdminDashboard,
    ManagerDashboard,
    StaffDashboard,
    SupplierDashboard
  ],
  templateUrl: './maindashboard.html',
  styleUrl: './maindashboard.css'
})
export class Maindashboard {
  constructor(public authService: AuthService) {}
}
