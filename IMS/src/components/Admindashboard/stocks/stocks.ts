import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StockService } from '../../../app/services/stock';
import { ProductService } from '../../../app/services/product';
import { WarehouseService } from '../../../app/services/warehouse';

@Component({
  selector: 'app-stocks',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './stocks.html',
  styleUrl: './stocks.css',
})
export class Stocks implements OnInit {
  private stockService = inject(StockService);
  private productService = inject(ProductService);
  private warehouseService = inject(WarehouseService);
  private cdr = inject(ChangeDetectorRef);

  stocks: any[] = [];
  filteredStocks: any[] = [];
  products: any[] = [];
  warehouses: any[] = [];

  showAddForm = false;
  showTransferForm = false;
  showUpdateForm = false;

  newStock = {
    product_id: null,
    warehouse_id: null,
    quantityOnHand: 0,
  };

  transferData = {
    fromWarehouseId: null,
    toWarehouseId: null,
    product_id: null,
    quantityOnHand: 0,
  };

  updateData = {
    id: null,
    product_id: null,
    warehouse_id: null,
    quantityOnHand: 0,
  };

  ngOnInit() {
    this.loadStocks();
    this.loadProducts();
    this.loadWarehouses();
  }

  loadStocks() {
    this.stockService.getAllStocks().subscribe({
      next: (data: any) => {
        this.stocks = data;
        this.filteredStocks = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('Error fetching stocks:', err);
        this.cdr.detectChanges();
      },
    });
  }

  loadProducts() {
    this.productService.getAllProducts().subscribe({
      next: (data: any) => {
        this.products = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('Error fetching products:', err);
        this.cdr.detectChanges();
      },
    });
  }

  loadWarehouses() {
    this.warehouseService.getAllWarehouses().subscribe({
      next: (data: any) => {
        this.warehouses = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('Error fetching warehouses:', err);
        this.cdr.detectChanges();
      },
    });
  }

  searchStock(event: any) {
    const term = event.target.value.toLowerCase();
    this.filteredStocks = this.stocks.filter(
      (s: any) =>
        s.productname?.toLowerCase().includes(term) ||
        s.warehousename?.toLowerCase().includes(term),
    );
  }

  openAddForm() {
    this.showAddForm = true;
    this.newStock = { product_id: null, warehouse_id: null, quantityOnHand: 0 };
  }

  closeAddForm() {
    this.showAddForm = false;
  }

  addStock() {
    this.stockService.addStock(this.newStock).subscribe({
      next: (res: any) => {
        this.loadStocks();
        this.closeAddForm();
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('Error adding stock:', err);
        this.cdr.detectChanges();
      },
    });
  }

  openTransferForm() {
    this.showTransferForm = true;
    this.transferData = {
      fromWarehouseId: null,
      toWarehouseId: null,
      product_id: null,
      quantityOnHand: 0,
    };
  }

  closeTransferForm() {
    this.showTransferForm = false;
  }

  transferStock() {
    this.stockService.transferStock(this.transferData).subscribe({
      next: (res: any) => {
        this.loadStocks();
        this.closeTransferForm();
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('Error transferring stock:', err);
        this.cdr.detectChanges();
      },
    });
  }

  openUpdateForm(stock: any) {
    this.showUpdateForm = true;

    // Find product and warehouse IDs from names or if DTO contains them (assume we can't find ID, user just inputs quantity)
    // Actually, StockResponsedto has productname and warehousename, not IDs.
    // For a real app, we need the IDs. We'll map by name for the default selections if needed.
    const product = this.products.find((p) => p.name === stock.productname);
    const warehouse = this.warehouses.find((w) => w.name === stock.warehousename);

    this.updateData = {
      id: stock.id,
      product_id: product ? product.id : null,
      warehouse_id: warehouse ? warehouse.id : null,
      quantityOnHand: stock.quantityOnHand,
    };
  }

  closeUpdateForm() {
    this.showUpdateForm = false;
  }

  updateStock() {
    if (this.updateData.id) {
      this.stockService.updateStock(this.updateData.id, this.updateData).subscribe({
        next: (res: any) => {
          this.loadStocks();
          this.closeUpdateForm();
          this.cdr.detectChanges();
        },
        error: (err: any) => {
          console.error('Error updating stock:', err);
          this.cdr.detectChanges();
        },
      });
    }
  }

  deleteStock(id: number) {
    if (confirm('Are you sure you want to delete this stock entry?')) {
      this.stockService.deleteStock(id).subscribe({
        next: (res: any) => {
          this.loadStocks();
          this.cdr.detectChanges();
        },
        error: (err: any) => {
          console.error('Error deleting stock:', err);
          this.cdr.detectChanges();
        },
      });
    }
  }
}
