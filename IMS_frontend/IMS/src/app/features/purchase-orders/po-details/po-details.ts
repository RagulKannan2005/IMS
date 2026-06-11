import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
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
  order?: PurchaseOrderResponseDto;
  items: PurchaseOrderItemResponseDto[] = [];
  loading = false;

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
      this.router.navigate(['/purchase-orders']);
    }
  }

  loadOrderDetails(): void {
    this.loading = true;
    
    // Fetch all POs and find this one
    this.poService.getAllPurchaseOrders().subscribe({
      next: (orders) => {
        this.order = orders.find(o => o.id === this.orderId);
        if (!this.order) {
          alert('Order not found.');
          this.router.navigate(['/purchase-orders']);
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
