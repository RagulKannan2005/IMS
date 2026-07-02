import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PurchaseOrderService } from '../../../app/services/purchaseorder';
import { Auth } from '../../../app/services/auth';

@Component({
  selector: 'app-supplier-purchase-orders',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './supplier-purchase-orders.html',
  styleUrl: './supplier-purchase-orders.css',
})
export class SupplierPurchaseOrders implements OnInit {
  orders: any[] = [];
  user: any;
  
  poService = inject(PurchaseOrderService);
  authService = inject(Auth);
  cdr = inject(ChangeDetectorRef);

  ngOnInit() {
    this.user = this.authService.getuser();
    this.loadOrders();
  }

  loadOrders() {
    if (!this.user || !this.user.supplierId) {
      alert("No Supplier ID found in your session! Please log out and log back in as a Supplier.");
      console.warn("User session data:", this.user);
      return;
    }

    this.poService.getPurchaseOrdersBySupplier(this.user.supplierId).subscribe({
      next: (response: any) => {
        if (response && response.data && Array.isArray(response.data)) {
          this.orders = response.data;
        } else if (response && response.value && Array.isArray(response.value)) {
          this.orders = response.value;
        } else if (Array.isArray(response)) {
          this.orders = response;
        } else {
          this.orders = [];
        }
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error fetching purchase orders', err);
      }
    });
  }

  updateStatus(order: any, newStatus: string) {
    this.poService.updateOrderStatus(order.id, newStatus).subscribe({
      next: () => {
        order.orderStatus = newStatus;
        alert('Order status updated successfully');
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error updating order status', err);
        alert('Failed to update order status');
      }
    });
  }
}
