import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { StockMovementService } from '../../../core/services/stock-movement.service';
import { ProductService } from '../../../core/services/product.service';
import { WarehouseService } from '../../../core/services/warehouse.service';
import { StockMovementResponseDto, StockMovementRequestDto, ProductResponseDto, WarehouseResponseDto } from '../../../shared/models/interfaces';

@Component({
  selector: 'app-stock-movement-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './stock-movement-list.html',
  styleUrls: ['./stock-movement-list.css']
})
export class StockMovementListComponent implements OnInit {
  movements = signal<StockMovementResponseDto[]>([]);
  products = signal<ProductResponseDto[]>([]);
  warehouses = signal<WarehouseResponseDto[]>([]);
  
  showModal = signal<boolean>(false);
  movementForm: FormGroup;

  // For UI filtering
  filterType = signal<string>('ALL');

  filteredMovements = computed(() => {
    let result = this.movements();
    if (this.filterType() !== 'ALL') {
      result = result.filter(m => m.movementType === this.filterType());
    }
    // Sort by descending date
    return result.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
  });

  constructor(
    private stockMovementService: StockMovementService,
    private productService: ProductService,
    private warehouseService: WarehouseService,
    private fb: FormBuilder
  ) {
    this.movementForm = this.fb.group({
      productId: [null, Validators.required],
      warehouseId: [null, Validators.required],
      movementType: ['IN', Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]],
      referenceNo: [''],
      remarks: ['']
    });
  }

  ngOnInit(): void {
    this.loadMovements();
    this.loadProducts();
    this.loadWarehouses();
  }

  loadMovements(): void {
    this.stockMovementService.getAllMovements().subscribe({
      next: (data: StockMovementResponseDto[]) => this.movements.set(data),
      error: (err: any) => console.error('Failed to load stock movements', err)
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

  setFilter(type: string): void {
    this.filterType.set(type);
  }

  openModal(): void {
    this.movementForm.reset({
      movementType: 'IN',
      quantity: 1
    });
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
  }

  saveMovement(): void {
    if (this.movementForm.invalid) {
      this.movementForm.markAllAsTouched();
      return;
    }

    const dto: StockMovementRequestDto = this.movementForm.value;
    
    this.stockMovementService.createMovement(dto).subscribe({
      next: () => {
        this.loadMovements();
        this.closeModal();
      },
      error: (err: any) => {
        console.error('Failed to record stock movement', err);
        alert('Failed to record movement. ' + (err.error?.message || 'Check console.'));
      }
    });
  }
}
