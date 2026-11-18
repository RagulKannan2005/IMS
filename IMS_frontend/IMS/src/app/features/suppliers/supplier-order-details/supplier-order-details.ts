import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { forkJoin } from 'rxjs';
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
    private poService: PurchaseOrderService,
    private cdr: ChangeDetectorRef
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
    
    forkJoin({
      order: this.poService.getPurchaseOrderById(this.orderId),
      items: this.poService.getItemsByPurchaseOrder(this.orderId)
    }).subscribe({
      next: ({ order, items }) => {
        this.order = order;
        this.items = items || [];
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error fetching PO details', err);
        alert('Could not load order details.');
        this.loading = false;
        this.cdr.detectChanges();
        this.router.navigate(['/supplier/orders']);
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
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Failed to accept order', err);
          alert('Error accepting order.');
          this.actionLoading = false;
          this.cdr.detectChanges();
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
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Failed to reject order', err);
          alert('Error rejecting order.');
          this.actionLoading = false;
          this.cdr.detectChanges();
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
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Failed to ship order', err);
          alert('Error shipping order.');
          this.actionLoading = false;
          this.cdr.detectChanges();
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
