import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { PurchaseOrderService } from '../../../core/services/purchase-order.service';
import { PurchaseOrderResponseDto } from '../../../shared/models/interfaces';

@Component({
  selector: 'app-ship-order',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterLink],
  templateUrl: './ship-order.html',
  styleUrl: './ship-order.css'
})
export class ShipOrder implements OnInit {
  orderId!: number;
  purchaseOrder?: PurchaseOrderResponseDto;
  shipForm: FormGroup;
  loading = false;
  submitting = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    private poService: PurchaseOrderService
  ) {
    this.shipForm = this.fb.group({
      carrierName: ['', Validators.required],
      trackingNumber: ['', Validators.required],
      remarks: ['']
    });
  }

  ngOnInit(): void {
    this.orderId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.orderId) {
      this.loadOrder();
    } else {
      this.router.navigate(['/supplier/orders']);
    }
  }

  loadOrder(): void {
    this.loading = true;
    this.poService.getAllPurchaseOrders().subscribe({
      next: (orders: PurchaseOrderResponseDto[]) => {
        this.purchaseOrder = orders.find((o: PurchaseOrderResponseDto) => o.id === this.orderId);
        this.loading = false;
      },
      error: (err: any) => {
        console.error('Failed to load order metadata', err);
        this.loading = false;
      }
    });
  }

  submit(): void {
    if (this.shipForm.invalid || this.submitting) {
      this.shipForm.markAllAsTouched();
      return;
    }

    this.submitting = true;

    // Use updateStatus to change status to SHIPPED
    this.poService.updateStatus(this.orderId, 'SHIPPED').subscribe({
      next: (res: any) => {
        alert('Order marked as shipped successfully!');
        this.router.navigate(['/supplier/orders']);
        this.submitting = false;
      },
      error: (err: any) => {
        console.error('Failed to ship order', err);
        alert('Failed to mark order as shipped. Please try again.');
        this.submitting = false;
      }
    });
  }
}
