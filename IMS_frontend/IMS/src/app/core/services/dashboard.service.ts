import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ProductResponseDto, StockMovementResponseDto } from '../../shared/models/interfaces';

export interface AuditLogResponseDto {
  user: string;
  action: string;
  target: string;
  time: string;
  status: string;
}

export interface SystemHealthDto {
  name: string;
  value: string;
  status: string;
}

export interface AdminDashboardDto {
  totalUsers: number;
  totalProducts: number;
  totalSuppliers: number;
  totalWarehouses: number;
  totalPurchaseOrders: number;
  lowStockProducts: ProductResponseDto[];
  auditLogs: AuditLogResponseDto[];
  platformStatus: SystemHealthDto[];
}

export interface ManagerDashboardDto {
  pendingPurchaseOrders: number;
  incomingShipments: number;
  activeSuppliers: number;
  lowStockProducts: ProductResponseDto[];
  recentOrders: any[]; // Or PurchaseOrderResponseDto[]
}

export interface StaffDashboardDto {
  stockInToday: number;
  stockOutToday: number;
  stockMovementsToday: StockMovementResponseDto[];
}

export interface SupplierDashboardDto {
  myProducts: number;
  myOrders: number;
  shippedOrders: number;
  pendingOrders: number;
}

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private readonly apiUrl = 'http://localhost:8082/api/v1/dashboards';

  constructor(private http: HttpClient) {}

  getAdminDashboard(): Observable<AdminDashboardDto> {
    return this.http.get<AdminDashboardDto>(`${this.apiUrl}/admin`);
  }

  getManagerDashboard(): Observable<ManagerDashboardDto> {
    return this.http.get<ManagerDashboardDto>(`${this.apiUrl}/manager`);
  }

  getStaffDashboard(): Observable<StaffDashboardDto> {
    return this.http.get<StaffDashboardDto>(`${this.apiUrl}/staff`);
  }

  getSupplierDashboard(): Observable<SupplierDashboardDto> {
    return this.http.get<SupplierDashboardDto>(`${this.apiUrl}/supplier`);
  }
}
