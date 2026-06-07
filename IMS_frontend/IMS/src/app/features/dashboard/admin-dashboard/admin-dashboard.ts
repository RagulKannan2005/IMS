import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardService, AdminDashboardDto } from '../../../core/services/dashboard.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-dashboard.html'
})
export class AdminDashboard implements OnInit {
  dashboardData = signal<AdminDashboardDto | null>(null);

  stats = computed(() => {
    const data = this.dashboardData();
    if (!data) {
      return [
        { title: 'Total Users', value: '...', subtext: 'Loading...', icon: 'users', color: '#6366f1' },
        { title: 'Total Products', value: '...', subtext: 'Loading...', icon: 'products', color: '#10b981' },
        { title: 'Suppliers', value: '...', subtext: 'Loading...', icon: 'suppliers', color: '#06b6d4' },
        { title: 'Warehouses', value: '...', subtext: 'Loading...', icon: 'warehouses', color: '#f59e0b' },
        { title: 'Purchase Orders', value: '...', subtext: 'Loading...', icon: 'orders', color: '#ec4899' }
      ];
    }
    return [
      { title: 'Total Users', value: String(data.totalUsers), subtext: 'Registered accounts', icon: 'users', color: '#6366f1' },
      { title: 'Total Products', value: String(data.totalProducts), subtext: 'Catalog items', icon: 'products', color: '#10b981' },
      { title: 'Suppliers', value: String(data.totalSuppliers), subtext: 'Registered partners', icon: 'suppliers', color: '#06b6d4' },
      { title: 'Warehouses', value: String(data.totalWarehouses), subtext: 'Active storage units', icon: 'warehouses', color: '#f59e0b' },
      { title: 'Purchase Orders', value: String(data.totalPurchaseOrders), subtext: 'Lifetime orders logged', icon: 'orders', color: '#ec4899' }
    ];
  });

  auditLogs = computed(() => this.dashboardData()?.auditLogs || []);

  systemStats = computed(() => this.dashboardData()?.platformStatus || []);

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.dashboardService.getAdminDashboard().subscribe({
      next: (data) => {
        this.dashboardData.set(data);
      },
      error: (err) => {
        console.error('Failed to load admin dashboard stats', err);
      }
    });
  }
}
