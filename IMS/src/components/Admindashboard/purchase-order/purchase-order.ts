import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-purchase-order',
  imports: [CommonModule, FormsModule],
  templateUrl: './purchase-order.html',
  styleUrl: './purchase-order.css',
})
export class PurchaseOrder {
  showform = false;
  isLoading = false;
  purchaseOrders: any[] = [];
  
  // Temporary mock data until APIs are wired
  suppliers: any[] = [
    { id: 1, name: 'Global Tech Suppliers' }, 
    { id: 2, name: 'Office Depot' }
  ];
  products: any[] = [
    { id: 1, name: 'Dell XPS 15', costPrice: 1200 },
    { id: 2, name: 'Logitech Mouse', costPrice: 45 }
  ];

  newPO: any = {
    poNumber: '',
    supplierId: null,
    expectedDeliveryDate: '',
    remarks: '',
    status: 'PENDING',
    items: []
  };

  openform() {
    this.showform = true;
    this.newPO.poNumber = 'PO-' + Math.floor(Math.random() * 100000);
  }

  closeform() {
    this.showform = false;
    this.newPO = { poNumber: '', supplierId: null, expectedDeliveryDate: '', remarks: '', status: 'PENDING', items: [] };
  }

  addItem() {
    this.newPO.items.push({
      productId: null,
      quantityOrdered: 1,
      unitCost: 0
    });
  }

  removeItem(index: number) {
    this.newPO.items.splice(index, 1);
  }

  onProductSelect(item: any) {
    const product = this.products.find(p => p.id === item.productId);
    if (product) {
      item.unitCost = product.costPrice;
    }
  }

  calculateTotal(): number {
    if (!this.newPO.items) return 0;
    return this.newPO.items.reduce((total: number, item: any) => {
      return total + (item.quantityOrdered * item.unitCost);
    }, 0);
  }

  savePurchaseOrder() {
    console.log("Saving PO: ", this.newPO);
    // TODO: Wire up Backend API
    this.closeform();
  }
}
