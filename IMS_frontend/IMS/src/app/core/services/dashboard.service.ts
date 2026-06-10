import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ProductResponseDto, StockMovementResponseDto, PurchaseOrderResponseDto } from '../../shared/models/interfaces';

export interface ProductSummaryDto {
  id: number;
  sku: string;
  name: string;
  stockQuantity: number;
}

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
  totalInternalProducts: number;
  recentInternalProducts: ProductSummaryDto[];
  auditLogs: AuditLogResponseDto[];
  platformStatus: SystemHealthDto[];
}

export interface ManagerDashboardDto {
  pendingPurchaseOrders: number;
  incomingShipments: number;
  activeSuppliers: number;
  lowStockProducts: ProductResponseDto[];
  totalInternalProducts: number;
  recentInternalProducts: ProductSummaryDto[];
  recentOrders: PurchaseOrderResponseDto[];
}

export interface StaffDashboardDto {
  stockInToday: number;
  stockOutToday: number;
  stockMovementsToday: StockMovementResponseDto[];
  lowStockProducts: ProductResponseDto[];
  totalInternalProducts: number;
  recentInternalProducts: ProductSummaryDto[];
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
