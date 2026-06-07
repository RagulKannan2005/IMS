import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardService, ManagerDashboardDto } from '../../../core/services/dashboard.service';
import { ProductResponseDto } from '../../../shared/models/interfaces';

@Component({
  selector: 'app-manager-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './manager-dashboard.html'
})
export class ManagerDashboard implements OnInit {
  dashboardData = signal<ManagerDashboardDto | null>(null);

  stats = computed(() => {
    const data = this.dashboardData();
    if (!data) {
      return [
        { title: 'Active Purchase Orders', value: '...', subtext: 'Loading...', icon: 'orders', color: '#6366f1' },
        { title: 'Incoming Shipments', value: '...', subtext: 'Loading...', icon: 'shipments', color: '#10b981' },
        { title: 'Active Suppliers', value: '...', subtext: 'Loading...', icon: 'suppliers', color: '#06b6d4' },
        { title: 'Low Stock Alerts', value: '...', subtext: 'Loading...', icon: 'stock', color: '#ef4444' }
      ];
    }
    return [
      { title: 'Active Purchase Orders', value: String(data.pendingPurchaseOrders), subtext: 'Awaiting completion', icon: 'orders', color: '#6366f1' },
      { title: 'Incoming Shipments', value: String(data.incomingShipments), subtext: 'Scheduled deliveries', icon: 'shipments', color: '#10b981' },
      { title: 'Active Suppliers', value: String(data.activeSuppliers), subtext: 'Verified suppliers', icon: 'suppliers', color: '#06b6d4' },
      { title: 'Low Stock Alerts', value: String(data.lowStockProducts.length), subtext: 'Below minimum limit', icon: 'stock', color: '#ef4444' }
    ];
  });

  lowStockProducts = computed<ProductResponseDto[]>(() => {
    return this.dashboardData()?.lowStockProducts || [];
  });

  recentOrders = computed<any[]>(() => {
    return this.dashboardData()?.recentOrders || [];
  });

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.dashboardService.getManagerDashboard().subscribe({
      next: (data) => {
        this.dashboardData.set(data);
      },
      error: (err) => {
        console.error('Failed to load manager dashboard stats', err);
      }
    });
  }
}
