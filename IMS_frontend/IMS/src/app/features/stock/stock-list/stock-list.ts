import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { StockService } from '../../../core/services/stock.service';
import { ProductService } from '../../../core/services/product.service';
import { WarehouseService } from '../../../core/services/warehouse.service';
import { StockResponseDto, StockRequestDto, StockTransferRequestDto, ProductResponseDto, WarehouseResponseDto } from '../../../shared/models/interfaces';

@Component({
  selector: 'app-stock-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './stock-list.html',
  styleUrls: ['./stock-list.css']
})
export class StockListComponent implements OnInit {
  stocks = signal<StockResponseDto[]>([]);
  products = signal<ProductResponseDto[]>([]);
  warehouses = signal<WarehouseResponseDto[]>([]);
  
  showInitModal = signal<boolean>(false);
  showTransferModal = signal<boolean>(false);
  
  initForm: FormGroup;
  transferForm: FormGroup;

  enrichedStocks = computed(() => {
    return this.stocks().map(stock => {
      // Find the product to get its reorderLevel
      const product = this.products().find(p => p.name === stock.productName);
      return {
        ...stock,
        reorderLevel: product?.reorderLevel || 0,
        sku: product?.sku || 'Unknown'
      };
    });
  });

  constructor(
    private stockService: StockService,
    private productService: ProductService,
    private warehouseService: WarehouseService,
    private fb: FormBuilder
  ) {
    this.initForm = this.fb.group({
      productId: [null, Validators.required],
      warehouseId: [null, Validators.required],
      quantityOnHand: [0, [Validators.required, Validators.min(0)]]
    });

    this.transferForm = this.fb.group({
      fromWarehouseId: [null, Validators.required],
      toWarehouseId: [null, Validators.required],
      productId: [null, Validators.required],
      quantityOnHand: [1, [Validators.required, Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    this.loadStocks();
    this.loadProducts();
    this.loadWarehouses();
  }

  loadStocks(): void {
    this.stockService.getAllStocks().subscribe({
      next: (data: StockResponseDto[]) => this.stocks.set(data),
      error: (err: any) => console.error('Failed to load stocks', err)
    });
  }

  loadProducts(): void {
    this.productService.getAllProducts().subscribe({
      next: (data: ProductResponseDto[]) => this.products.set(data),
      error: (err: any) => console.error('Failed to load products', err)
    });
  }

  loadWarehouses(): void {
    this.warehouseService.getAllWarehouses().subscribe({
      next: (data: WarehouseResponseDto[]) => this.warehouses.set(data),
      error: (err: any) => console.error('Failed to load warehouses', err)
    });
  }

  openInitModal(): void {
    this.initForm.reset({ quantityOnHand: 0 });
    this.showInitModal.set(true);
  }

  closeInitModal(): void {
    this.showInitModal.set(false);
  }

  saveInitStock(): void {
    if (this.initForm.invalid) {
      this.initForm.markAllAsTouched();
      return;
    }

    const dto: StockRequestDto = this.initForm.value;
    this.stockService.addStock(dto).subscribe({
      next: () => {
        this.loadStocks();
        this.closeInitModal();
      },
      error: (err: any) => console.error('Failed to initialize stock', err)
    });
  }

  openTransferModal(stock?: StockResponseDto): void {
    this.transferForm.reset({ quantityOnHand: 1 });
    
    if (stock) {
      // Reverse lookup IDs from names
      const product = this.products().find(p => p.name === stock.productName);
      const warehouse = this.warehouses().find(w => w.name === stock.warehouseName);
      
      this.transferForm.patchValue({
        productId: product?.id,
        fromWarehouseId: warehouse?.id
      });
    }
    
    this.showTransferModal.set(true);
  }

  closeTransferModal(): void {
    this.showTransferModal.set(false);
  }

  saveTransferStock(): void {
    if (this.transferForm.invalid) {
      this.transferForm.markAllAsTouched();
      return;
    }

    const dto: StockTransferRequestDto = this.transferForm.value;
    if (dto.fromWarehouseId === dto.toWarehouseId) {
      alert("Source and destination warehouses cannot be the same.");
      return;
    }

    this.stockService.transferStock(dto).subscribe({
      next: () => {
        this.loadStocks();
        this.closeTransferModal();
      },
      error: (err: any) => {
        console.error('Failed to transfer stock', err);
        alert('Failed to transfer stock. ' + (err.error?.message || 'Check console for details.'));
      }
    });
  }
}
