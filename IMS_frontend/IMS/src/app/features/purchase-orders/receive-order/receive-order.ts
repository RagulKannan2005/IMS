import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { PurchaseOrderService } from '../../../core/services/purchase-order.service';
import { WarehouseService } from '../../../core/services/warehouse.service';
import { PurchaseOrderResponseDto, PurchaseOrderItemResponseDto, WarehouseResponseDto } from '../../../shared/models/interfaces';

@Component({
  selector: 'app-receive-order',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterLink],
  templateUrl: './receive-order.html',
  styleUrl: './receive-order.css'
})
export class ReceiveOrder implements OnInit {
  orderId!: number;
  purchaseOrder?: PurchaseOrderResponseDto;
  orderItems: PurchaseOrderItemResponseDto[] = [];
  warehouses: WarehouseResponseDto[] = [];
  receiveForm: FormGroup;
  loading = false;
  submitting = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    private poService: PurchaseOrderService,
    private warehouseService: WarehouseService,
    private cdr: ChangeDetectorRef
  ) {
    this.receiveForm = this.fb.group({
      warehouseId: ['', Validators.required],
      items: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.orderId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.orderId) {
      this.loadData();
    } else {
      this.router.navigate(['/purchase-orders']);
    }
  }

  get itemsFormArray(): FormArray {
    return this.receiveForm.get('items') as FormArray;
  }

  loadData(): void {
    this.loading = true;

    forkJoin({
      order: this.poService.getPurchaseOrderById(this.orderId),
      items: this.poService.getItemsByPurchaseOrder(this.orderId),
      warehouses: this.warehouseService.getWarehousesByActiveStatus('active')
    }).subscribe({
      next: ({ order, items, warehouses }) => {
        this.purchaseOrder = order;
        this.orderItems = items || [];
        this.warehouses = warehouses || [];
        this.buildFormArray();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('Failed to load PO data', err);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  buildFormArray(): void {
    this.itemsFormArray.clear();
    this.orderItems.forEach(item => {
      const remaining = item.quantityOrdered - item.quantityReceived;
      this.itemsFormArray.push(this.fb.group({
        itemId: [item.id],
        productId: [item.productId],
        productName: [item.productName],
        quantityOrdered: [item.quantityOrdered],
        quantityAlreadyReceived: [item.quantityReceived],
        quantityReceived: [remaining, [Validators.required, Validators.min(0), Validators.max(remaining)]]
      }));
    });
  }

  fillFullQuantities(): void {
    this.itemsFormArray.controls.forEach(control => {
      const remaining = control.get('quantityOrdered')?.value - control.get('quantityAlreadyReceived')?.value;
      control.patchValue({ quantityReceived: remaining });
    });
  }

  clearQuantities(): void {
    this.itemsFormArray.controls.forEach(control => {
      control.patchValue({ quantityReceived: 0 });
    });
  }

  submit(): void {
    if (this.receiveForm.invalid || this.submitting) {
      this.receiveForm.markAllAsTouched();
      return;
    }

    this.submitting = true;
    
    this.poService.receivePurchaseOrder(this.orderId).subscribe({
      next: (res: any) => {
        alert('Purchase order received successfully!');
        this.router.navigate(['/purchase-orders']);
        this.submitting = false;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('Failed to receive order', err);
        alert('Failed to process receiving. Please verify details.');
        this.submitting = false;
        this.cdr.detectChanges();
      }
    });
  }
}
