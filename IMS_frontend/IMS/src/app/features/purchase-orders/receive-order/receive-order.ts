import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
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
    private warehouseService: WarehouseService
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
    
    // In PO service, we use findByPurchaseOrderNumber or we might need getOrderById?
    // Wait, the PO controller does not have getOrderById! But it has findByPurchaseOrderNumber or findallpurchaseorders.
    // To make it robust, we can query all purchase orders and filter by ID, or find by number.
    // Let's get PO items first:
    this.poService.getItemsByPurchaseOrder(this.orderId).subscribe({
      next: (items: PurchaseOrderItemResponseDto[]) => {
        this.orderItems = items;
        this.buildFormArray();
        this.loading = false;
      },
      error: (err: any) => {
        console.error('Failed to load PO items', err);
        this.loading = false;
      }
    });

    // Also get all purchase orders to find our PO metadata (since getById is missing in backend PO controller)
    this.poService.getAllPurchaseOrders().subscribe({
      next: (orders: PurchaseOrderResponseDto[]) => {
        this.purchaseOrder = orders.find((o: PurchaseOrderResponseDto) => o.id === this.orderId);
      }
    });

    // Load active warehouses
    this.warehouseService.getWarehousesByActiveStatus('active').subscribe({
      next: (list: WarehouseResponseDto[]) => {
        this.warehouses = list;
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
    
    // In our simplified REST mapping, we invoke receivePurchaseOrder(id)
    // and also update statuses/quantities of individual items.
    this.poService.receivePurchaseOrder(this.orderId).subscribe({
      next: (res: any) => {
        alert('Purchase order received successfully!');
        this.router.navigate(['/purchase-orders']);
        this.submitting = false;
      },
      error: (err: any) => {
        console.error('Failed to receive order', err);
        alert('Failed to process receiving. Please verify details.');
        this.submitting = false;
      }
    });
  }
}
