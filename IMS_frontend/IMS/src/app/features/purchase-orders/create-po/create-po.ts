import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { PurchaseOrderService } from '../../../core/services/purchase-order.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-create-po',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './create-po.html',
  styleUrl: './create-po.css'
})
export class CreatePurchaseOrderComponent implements OnInit {
  suppliers: any[] = [];
  supplierProducts: any[] = [];
  
  selectedSupplierId: number | null = null;
  selectedProduct: any = null;
  
  orderForm = {
    quantity: 1,
    expectedDeliveryDate: '',
    remarks: ''
  };

  loading = false;
  submitting = false;
  error = '';
  success = '';

  private readonly apiUrl = 'http://localhost:8082';

  constructor(
    private http: HttpClient,
    private poService: PurchaseOrderService,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadSuppliers();
    
    // Set default expected delivery date to 7 days from now
    const nextWeek = new Date();
    nextWeek.setDate(nextWeek.getDate() + 7);
    this.orderForm.expectedDeliveryDate = nextWeek.toISOString().split('T')[0];
  }

  loadSuppliers(): void {
    this.loading = true;
    this.http.get<any[]>(`${this.apiUrl}/api/v1/suppliers/getallsuppliers`).subscribe({
      next: (data) => {
        this.suppliers = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load suppliers.';
        this.loading = false;
        console.error(err);
      }
    });
  }

  onSupplierChange(): void {
    this.supplierProducts = [];
    this.selectedProduct = null;
    
    if (this.selectedSupplierId) {
      this.loading = true;
      this.http.get<any[]>(`${this.apiUrl}/api/supplier-products/supplier/${this.selectedSupplierId}`).subscribe({
        next: (data) => {
          this.supplierProducts = data;
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Failed to load supplier products.';
          this.loading = false;
          console.error(err);
        }
      });
    }
  }

  onProductChange(product: any): void {
    this.selectedProduct = product;
    // Auto-adjust quantity if it exceeds availability
    if (this.orderForm.quantity > this.selectedProduct.availableQuantity) {
      this.orderForm.quantity = this.selectedProduct.availableQuantity;
    }
  }

  onSubmit(): void {
    if (!this.selectedSupplierId || !this.selectedProduct) {
      this.error = 'Please select a supplier and a product.';
      return;
    }
    
    if (this.orderForm.quantity <= 0) {
      this.error = 'Quantity must be greater than 0.';
      return;
    }
    
    if (this.orderForm.quantity > this.selectedProduct.availableQuantity) {
      this.error = `Quantity cannot exceed supplier's available stock (${this.selectedProduct.availableQuantity}).`;
      return;
    }

    this.error = '';
    this.success = '';
    this.submitting = true;

    // Retrieve user ID from AuthService session
    let createdBy = this.authService.currentUser()?.id || 1;

    const totalAmount = this.selectedProduct.unitPrice * this.orderForm.quantity;

    // 1. Create Purchase Order
    const poRequest = {
      poNumber: 'PO-' + Math.floor(100000 + Math.random() * 900000), // Random PO number
      supplierId: this.selectedSupplierId,
      createdBy: createdBy,
      totalAmount: totalAmount,
      status: 'ORDERED',
      orderedAt: new Date().toISOString().split('T')[0],
      expectedDeliveryDate: this.orderForm.expectedDeliveryDate,
      remarks: this.orderForm.remarks
    };

    this.http.post<any>(`${this.apiUrl}/api/v1/purchase-orders/addpurchaseorder`, poRequest).subscribe({
      next: (poResponse) => {
        // 2. Add Purchase Order Item
        const itemRequest = {
          purchaseOrderId: poResponse.id,
          productId: this.selectedProduct.productId,
          quantityOrdered: this.orderForm.quantity,
          quantityReceived: 0,
          unitCost: this.selectedProduct.unitPrice
        };

        this.http.post<any>(`${this.apiUrl}/api/v1/purchaseorderitems`, itemRequest).subscribe({
          next: () => {
            this.success = 'Purchase order created successfully!';
            this.submitting = false;
            setTimeout(() => {
              this.router.navigate(['/purchase-orders']);
            }, 1500);
          },
          error: (err) => {
            this.error = err.error?.message || 'Failed to add items to purchase order.';
            this.submitting = false;
            console.error(err);
          }
        });
      },
      error: (err) => {
        this.error = err.error?.message || 'Failed to create purchase order.';
        this.submitting = false;
        console.error(err);
      }
    });
  }
}
