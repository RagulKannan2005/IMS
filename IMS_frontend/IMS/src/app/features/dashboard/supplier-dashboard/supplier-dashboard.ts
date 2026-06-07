import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardService, SupplierDashboardDto } from '../../../core/services/dashboard.service';
import { SupplierService } from '../../../core/services/supplier.service';
import { ProductResponseDto, PurchaseOrderResponseDto } from '../../../shared/models/interfaces';

@Component({
  selector: 'app-supplier-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './supplier-dashboard.html'
})
export class SupplierDashboard implements OnInit {
  dashboardData = signal<SupplierDashboardDto | null>(null);
  myProductsList = signal<ProductResponseDto[]>([]);
  dispatchedOrders = signal<PurchaseOrderResponseDto[]>([]);

  stats = computed(() => {
    const data = this.dashboardData();
    if (!data) {
      return [
        { title: 'My Supplied Products', value: '...', subtext: 'Products catalog registered', icon: 'products', color: '#6366f1' },
        { title: 'Active Orders', value: '...', subtext: 'Total assigned purchase orders', icon: 'orders', color: '#f59e0b' },
        { title: 'Shipped Orders', value: '...', subtext: 'Fulfilled shipments count', icon: 'shipments', color: '#10b981' },
        { title: 'Pending Dispatch', value: '...', subtext: 'Awaiting shipping logistics', icon: 'stock', color: '#06b6d4' }
      ];
    }
    return [
      { title: 'My Supplied Products', value: `${data.myProducts} Items`, subtext: 'Products catalog registered', icon: 'products', color: '#6366f1' },
      { title: 'Active Orders', value: `${data.myOrders} Orders`, subtext: 'Total assigned purchase orders', icon: 'orders', color: '#f59e0b' },
      { title: 'Shipped Orders', value: `${data.shippedOrders} Orders`, subtext: 'Fulfilled shipments count', icon: 'shipments', color: '#10b981' },
      { title: 'Pending Dispatch', value: `${data.pendingOrders} Orders`, subtext: 'Awaiting shipping logistics', icon: 'stock', color: '#06b6d4' }
    ];
  });

  constructor(
    private dashboardService: DashboardService,
    private supplierService: SupplierService
  ) {}

  ngOnInit(): void {
    this.dashboardService.getSupplierDashboard().subscribe({
      next: (data) => {
        this.dashboardData.set(data);
      },
      error: (err) => {
        console.error('Failed to load supplier dashboard stats', err);
      }
    });

    this.supplierService.getMyProducts().subscribe({
      next: (list) => {
        this.myProductsList.set(list);
      },
      error: (err) => {
        console.error('Failed to load supplier products catalog', err);
      }
    });

    this.supplierService.getMyOrders().subscribe({
      next: (list) => {
        this.dispatchedOrders.set(list);
      },
      error: (err) => {
        console.error('Failed to load supplier purchase orders', err);
      }
    });
  }
}
