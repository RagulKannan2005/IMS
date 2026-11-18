import { Component, OnInit, signal, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { forkJoin } from 'rxjs';
import { PurchaseOrderService } from '../../../core/services/purchase-order.service';
import { PurchaseOrderResponseDto, PurchaseOrderItemResponseDto } from '../../../shared/models/interfaces';

@Component({
  selector: 'app-po-details',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './po-details.html',
  styleUrl: './po-details.css'
})
export class PurchaseOrderDetailsComponent implements OnInit {
  orderId!: number;
  order = signal<PurchaseOrderResponseDto | null>(null);
  items = signal<PurchaseOrderItemResponseDto[]>([]);
  loading = signal<boolean>(false);
  errorMessage = signal<string | null>(null);

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
      this.router.navigate(['/purchase-orders']);
    }
  }

  loadOrderDetails(): void {
    this.loading.set(true);
    this.errorMessage.set(null);

    forkJoin({
      order: this.poService.getPurchaseOrderById(this.orderId),
      items: this.poService.getItemsByPurchaseOrder(this.orderId)
    }).subscribe({
      next: ({ order, items }) => {
        this.order.set(order);
        this.items.set(items || []);
        this.loading.set(false);
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Error fetching PO details', err);
        this.errorMessage.set('Could not load purchase order details.');
        this.loading.set(false);
        this.cdr.markForCheck();
      }
    });
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
      case 'COMPLETED':
        return 'success';
      case 'REJECTED':
      case 'CANCELLED':
        return 'danger';
      default:
        return 'secondary';
    }
  }
}
