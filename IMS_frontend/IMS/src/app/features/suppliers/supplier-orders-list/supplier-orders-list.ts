import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { PurchaseOrderService } from '../../../core/services/purchase-order.service';
import { SupplierService } from '../../../core/services/supplier.service';
import { PurchaseOrderResponseDto } from '../../../shared/models/interfaces';

@Component({
  selector: 'app-supplier-orders-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './supplier-orders-list.html',
  styleUrl: './supplier-orders-list.css'
})
export class SupplierOrderListComponent implements OnInit {
  orders: PurchaseOrderResponseDto[] = [];
  loading = false;
  actionLoading: { [key: number]: boolean } = {};

  constructor(
    private poService: PurchaseOrderService,
    private supplierService: SupplierService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading = true;
    console.log('[loadOrders] Fetching orders...');
    this.supplierService.getMyOrders().subscribe({
      next: (data) => {
        console.log('[loadOrders] Received data:', data);
        try {
          this.orders = (data || []).sort((a, b) => (b.id || 0) - (a.id || 0));
        } catch(e) {
          console.error('[loadOrders] Error sorting data:', e);
        }
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('[loadOrders] Failed to load orders:', err);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  acceptOrder(id: number): void {
    if (confirm('Are you sure you want to accept this order?')) {
      this.actionLoading[id] = true;
      this.poService.acceptOrder(id).subscribe({
        next: () => {
          this.loadOrders();
          this.actionLoading[id] = false;
        },
        error: (err) => {
          console.error('Failed to accept order', err);
          alert('Error accepting order.');
          this.actionLoading[id] = false;
        }
      });
    }
  }

  rejectOrder(id: number): void {
    if (confirm('Are you sure you want to reject this order?')) {
      this.actionLoading[id] = true;
      this.poService.rejectOrder(id).subscribe({
        next: () => {
          this.loadOrders();
          this.actionLoading[id] = false;
        },
        error: (err) => {
          console.error('Failed to reject order', err);
          alert('Error rejecting order.');
          this.actionLoading[id] = false;
        }
      });
    }
  }

  shipOrder(id: number): void {
    if (confirm('Mark this order as shipped?')) {
      this.actionLoading[id] = true;
      this.poService.shipOrder(id).subscribe({
        next: () => {
          this.loadOrders();
          this.actionLoading[id] = false;
        },
        error: (err) => {
          console.error('Failed to ship order', err);
          alert('Error shipping order.');
          this.actionLoading[id] = false;
        }
      });
    }
  }

  getStatusClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'ORDERED':
      case 'PENDING':
        return 'warning';
      case 'ACCEPTED':
        return 'info';
      case 'SHIPPED':
        return 'primary';
      case 'RECEIVED':
        return 'success';
      case 'REJECTED':
      case 'CANCELLED':
        return 'danger';
      default:
        return 'secondary';
    }
  }
}
