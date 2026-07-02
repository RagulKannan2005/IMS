import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PurchaseOrderService } from '../../../app/services/purchaseorder';
import { WarehouseService } from '../../../app/services/warehouse';
import { SupplierService } from '../../../app/services/supplier';
import { SupplierProductService } from '../../../app/services/supplier-product';

@Component({
  selector: 'app-purchase-order',
  imports: [CommonModule, FormsModule],
  templateUrl: './purchase-order.html',
  styleUrl: './purchase-order.css',
})
export class PurchaseOrder implements OnInit {
  showform = false;
  isLoading = false;
  purchaseOrders: any[] = [];
  
  warehouses: any[] = [];
  suppliers: any[] = [];
  products: any[] = []; // supplier products

  private purchaseOrderService = inject(PurchaseOrderService);
  private warehouseService = inject(WarehouseService);
  private supplierService = inject(SupplierService);
  private supplierProductService = inject(SupplierProductService);

  newPO: any = {
    poNumber: '',
    warehouseId: null,
    supplierId: null,
    expectedDeliveryDate: '',
    remarks: '',
    status: 'PENDING',
    items: []
  };

  ngOnInit() {
    this.loadWarehouses();
    this.loadPurchaseOrders();
  }

  loadWarehouses() {
    this.warehouseService.getAllWarehouses().subscribe({
      next: (res: any) => {
        this.warehouses = res;
      },
      error: (err: any) => console.error('Error loading warehouses', err)
    });
  }

  loadPurchaseOrders() {
    this.isLoading = true;
    this.purchaseOrderService.getAllPurchaseOrders().subscribe({
      next: (res: any) => {
        this.purchaseOrders = res;
        this.isLoading = false;
      },
      error: (err: any) => {
        console.error('Error loading POs', err);
        this.isLoading = false;
      }
    });
  }

  onWarehouseSelect() {
    this.suppliers = [];
    this.products = [];
    this.newPO.supplierId = null;
    this.newPO.items = [];
    
    if (this.newPO.warehouseId) {
      this.supplierService.getAllSuppliers().subscribe({
        next: (res: any) => {
          this.suppliers = res;
        },
        error: (err: any) => console.error('Error loading suppliers', err)
      });
    }
  }

  onSupplierSelect() {
    this.products = [];
    this.newPO.items = [];
    
    if (this.newPO.supplierId) {
      this.supplierProductService.getProductsBySupplier(this.newPO.supplierId).subscribe({
        next: (res: any) => {
          this.products = res;
        },
        error: (err: any) => console.error('Error loading supplier products', err)
      });
    }
  }

  openform() {
    this.showform = true;
    this.newPO.poNumber = 'PO-' + Math.floor(Math.random() * 100000);
    this.newPO.items = [];
  }

  closeform() {
    this.showform = false;
    this.newPO = { poNumber: '', warehouseId: null, supplierId: null, expectedDeliveryDate: '', remarks: '', status: 'PENDING', items: [] };
    this.suppliers = [];
    this.products = [];
  }

  addItem() {
    this.newPO.items.push({
      supplierProductId: null,
      quantityOrdered: 1,
      quantityReceived: 0,
      unitCost: 0,
      orderStatus: 'PENDING'
    });
  }

  removeItem(index: number) {
    this.newPO.items.splice(index, 1);
  }

  onProductSelect(item: any) {
    const product = this.products.find(p => p.id === item.supplierProductId);
    if (product) {
      item.unitCost = product.costPrice || 0; 
    }
  }

  calculateTotal(): number {
    if (!this.newPO.items) return 0;
    return this.newPO.items.reduce((total: number, item: any) => {
      return total + (item.quantityOrdered * item.unitCost);
    }, 0);
  }

  savePurchaseOrder() {
    // In a real app, createdBy should be dynamic
    const payload = {
      ...this.newPO,
      createdBy: 1, 
      totalAmount: this.calculateTotal()
    };

    this.purchaseOrderService.addPurchaseOrder(payload).subscribe({
      next: (res: any) => {
        alert('Purchase Order Created Successfully!');
        this.closeform();
        this.loadPurchaseOrders();
      },
      error: (err: any) => {
        console.error('Error saving PO', err);
        alert('Failed to save Purchase Order.');
      }
    });
  }
}
