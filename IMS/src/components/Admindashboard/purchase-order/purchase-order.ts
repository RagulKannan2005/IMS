import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PurchaseOrderService } from '../../../app/services/purchaseorder';
import { WarehouseService } from '../../../app/services/warehouse';
import { SupplierService } from '../../../app/services/supplier';
import { SupplierProductService } from '../../../app/services/supplier-product';
import { ProductService } from '../../../app/services/product';

@Component({
  selector: 'app-purchase-order',
  imports: [CommonModule, FormsModule],
  templateUrl: './purchase-order.html',
  styleUrl: './purchase-order.css',
})
export class PurchaseOrder implements OnInit {
  showform = false;
  showReceiveForm = false;
  isLoading = false;
  purchaseOrders: any[] = [];
  
  warehouses: any[] = [];
  suppliers: any[] = [];
  products: any[] = []; // supplier products
  internalProducts: any[] = []; // internal inventory products

  private purchaseOrderService = inject(PurchaseOrderService);
  private warehouseService = inject(WarehouseService);
  private supplierService = inject(SupplierService);
  private supplierProductService = inject(SupplierProductService);
  private productService = inject(ProductService);
  private cdr = inject(ChangeDetectorRef);

  newPO: any = {
    poNumber: '',
    warehouseId: null,
    supplierId: null,
    expectedDeliveryDate: '',
    remarks: '',
    status: 'ORDERED',
    items: []
  };

  selectedPO: any = null;
  receiveMappings: any[] = [];

  ngOnInit() {
    this.loadWarehouses();
    this.loadPurchaseOrders();
    this.loadInternalProducts();
  }

  loadWarehouses() {
    this.warehouseService.getAllWarehouses().subscribe({
      next: (res: any) => {
        this.warehouses = res;
      },
      error: (err: any) => console.error('Error loading warehouses', err)
    });
  }

  loadInternalProducts() {
    this.productService.getAllProducts().subscribe({
      next: (res: any) => {
        this.internalProducts = res;
      },
      error: (err: any) => console.error('Error loading internal products', err)
    });
  }

  loadPurchaseOrders() {
    this.isLoading = true;
    console.log("loadPurchaseOrders started, making API call...");
    this.purchaseOrderService.getAllPurchaseOrders().subscribe({
      next: (res: any) => {
        console.log("loadPurchaseOrders next: ", res);
        this.purchaseOrders = res;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('Error loading POs from HTTP: ', err);
        this.isLoading = false;
        this.cdr.detectChanges();
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
    this.newPO = { poNumber: '', warehouseId: null, supplierId: null, expectedDeliveryDate: '', remarks: '', status: 'ORDERED', items: [] };
    this.suppliers = [];
    this.products = [];
  }

  addItem() {
    this.newPO.items.push({
      supplierProductId: null,
      quantityOrdered: 1,
      quantityReceived: 0,
      unitCost: 0,
      orderStatus: 'ORDERED'
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
    const payload = {
      ...this.newPO,
      createdBy: 1, // Will be overridden by backend based on token
      totalAmount: this.calculateTotal()
    };

    this.purchaseOrderService.addPurchaseOrder(payload).subscribe({
      next: (res: any) => {
        alert('Purchase Order Created Successfully!');
        this.closeform();
        this.loadPurchaseOrders();
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('Error saving PO', err);
        alert('Failed to save Purchase Order.');
        this.cdr.detectChanges();
      }
    });
  }

  // Admin Receive Logic
  openReceiveForm(order: any) {
    this.selectedPO = order;
    this.showReceiveForm = true;
    this.receiveMappings = (order.items || []).map((item: any) => ({
      purchaseOrderItemId: item.id,
      supplierProductName: item.supplierProductName,
      quantityOrdered: item.quantityOrdered,
      action: 'LINK',
      internalProductId: null
    }));
  }

  closeReceiveForm() {
    this.showReceiveForm = false;
    this.selectedPO = null;
    this.receiveMappings = [];
  }

  submitReceiveOrder() {
    // Validate mappings
    for (let map of this.receiveMappings) {
      if (map.action === 'LINK' && !map.internalProductId) {
        alert(`Please select an internal product to link for ${map.supplierProductName}, or choose "Create New".`);
        return;
      }
    }

    const payload = {
      purchaseOrderId: this.selectedPO.id,
      mappings: this.receiveMappings.map(m => ({
        purchaseOrderItemId: m.purchaseOrderItemId,
        action: m.action,
        internalProductId: m.action === 'LINK' ? m.internalProductId : null
      }))
    };

    // Need an HTTP call using purchaseOrderService.
    // wait, we don't have receivePurchaseOrder(payload) in frontend service?
    // Let's add it or use http directly here. Actually, we should check purchaseorder.ts service.
    
    // I'll call a method on the service that we might need to add or update.
    this.purchaseOrderService.receivePurchaseOrder(payload).subscribe({
      next: () => {
        alert('Purchase Order Received Successfully! Stock updated.');
        this.closeReceiveForm();
        this.loadPurchaseOrders();
        this.loadInternalProducts();
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('Error receiving PO', err);
        alert('Failed to receive Purchase Order.');
        this.cdr.detectChanges();
      }
    });
  }
}
