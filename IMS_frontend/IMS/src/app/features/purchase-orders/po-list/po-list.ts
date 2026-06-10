import { Component, OnInit } from '@angular/core';
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
  orders: PurchaseOrderResponseDto[] = [];
  loading = false;

  constructor(private poService: PurchaseOrderService) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading = true;
    this.poService.getAllPurchaseOrders().subscribe({
      next: (data) => {
        // Sort orders descending
        this.orders = data.sort((a, b) => b.id - a.id);
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load orders', err);
        this.loading = false;
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
