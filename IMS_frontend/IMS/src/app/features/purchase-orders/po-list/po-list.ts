import { Component, OnInit, signal, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { PurchaseOrderService } from '../../../core/services/purchase-order.service';
import { PurchaseOrderResponseDto } from '../../../shared/models/interfaces';

@Component({
  selector: 'app-po-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './po-list.html',
  styleUrl: './po-list.css'
})
export class PurchaseOrderListComponent implements OnInit {
  orders = signal<PurchaseOrderResponseDto[]>([]);
  loading = signal<boolean>(false);
  errorMessage = signal<string | null>(null);

  constructor(
    private poService: PurchaseOrderService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading.set(true);
    this.errorMessage.set(null);
    this.poService.getAllPurchaseOrders().subscribe({
      next: (data) => {
        const sorted = (data || []).sort((a, b) => (b.id || 0) - (a.id || 0));
        this.orders.set(sorted);
        this.loading.set(false);
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Failed to load orders', err);
        this.errorMessage.set('Failed to load purchase orders. Please try again.');
        this.loading.set(false);
        this.cdr.markForCheck();
      }
    });
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
