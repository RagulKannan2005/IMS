import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { PurchaseOrderService } from '../../../core/services/purchase-order.service';
import { PurchaseOrderResponseDto, PurchaseOrderItemResponseDto } from '../../../shared/models/interfaces';

@Component({
  selector: 'app-supplier-order-details',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './supplier-order-details.html',
  styleUrl: './supplier-order-details.css'
})
export class SupplierOrderDetailsComponent implements OnInit {
  orderId!: number;
  order?: PurchaseOrderResponseDto;
  items: PurchaseOrderItemResponseDto[] = [];
  loading = false;
  actionLoading = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private poService: PurchaseOrderService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.orderId = Number(idParam);
      this.loadOrderDetails();
    } else {
      this.router.navigate(['/supplier/orders']);
    }
  }

  loadOrderDetails(): void {
    this.loading = true;
    
    // Fetch all POs and find this one (since there's no getById endpoint for POs in backend)
    this.poService.getAllPurchaseOrders().subscribe({
      next: (orders) => {
        this.order = orders.find(o => o.id === this.orderId);
        if (!this.order) {
          alert('Order not found.');
          this.router.navigate(['/supplier/orders']);
          return;
        }

        // Fetch PO items
        this.poService.getItemsByPurchaseOrder(this.orderId).subscribe({
          next: (items) => {
            this.items = items;
            this.loading = false;
          },
          error: (err) => {
            console.error('Error fetching PO items', err);
            this.loading = false;
          }
        });
      },
      error: (err) => {
        console.error('Error fetching PO', err);
        this.loading = false;
      }
    });
  }

  acceptOrder(): void {
    if (confirm('Are you sure you want to accept this order?')) {
      this.actionLoading = true;
      this.poService.acceptOrder(this.orderId).subscribe({
        next: (updatedOrder) => {
          this.order = updatedOrder;
          this.actionLoading = false;
        },
        error: (err) => {
          console.error('Failed to accept order', err);
          alert('Error accepting order.');
          this.actionLoading = false;
        }
      });
    }
  }

  rejectOrder(): void {
    if (confirm('Are you sure you want to reject this order?')) {
      this.actionLoading = true;
      this.poService.rejectOrder(this.orderId).subscribe({
        next: (updatedOrder) => {
          this.order = updatedOrder;
          this.actionLoading = false;
        },
        error: (err) => {
          console.error('Failed to reject order', err);
          alert('Error rejecting order.');
          this.actionLoading = false;
        }
      });
    }
  }

  shipOrder(): void {
    if (confirm('Mark this order as shipped?')) {
      this.actionLoading = true;
      this.poService.shipOrder(this.orderId).subscribe({
        next: (updatedOrder) => {
          this.order = updatedOrder;
          this.actionLoading = false;
        },
        error: (err) => {
          console.error('Failed to ship order', err);
          alert('Error shipping order.');
          this.actionLoading = false;
        }
      });
    }
  }

  getStatusClass(status?: string): string {
    if (!status) return 'secondary';
    switch (status.toUpperCase()) {
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
