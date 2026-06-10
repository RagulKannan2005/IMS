import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { DashboardService, SupplierDashboardDto } from '../../../core/services/dashboard.service';
import { SupplierService } from '../../../core/services/supplier.service';
import { ProductService } from '../../../core/services/product.service';
import { CategoryService } from '../../../core/services/category.service';
import { ProductResponseDto, PurchaseOrderResponseDto, CategoryResponseDto, ProductRequestDto } from '../../../shared/models/interfaces';

import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-supplier-dashboard',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './supplier-dashboard.html',
  styleUrl: './supplier-dashboard.css'
})
export class SupplierDashboard implements OnInit {
  dashboardData = signal<SupplierDashboardDto | null>(null);
  myProductsList = signal<ProductResponseDto[]>([]);
  dispatchedOrders = signal<PurchaseOrderResponseDto[]>([]);
  categories = signal<CategoryResponseDto[]>([]);

  showAddModal = signal<boolean>(false);
  productForm: FormGroup;

  stats = computed(() => {
    const productsCount = this.myProductsList().length;
    const orders = this.dispatchedOrders();
    
    const pendingCount = orders.filter(o => o.status === 'ORDERED' || o.status === 'PENDING').length;
    const acceptedCount = orders.filter(o => o.status === 'ACCEPTED').length;
    const shippedCount = orders.filter(o => o.status === 'SHIPPED').length;

    return [
      { title: 'My Supplied Products', value: `${productsCount} Items`, subtext: 'Products catalog registered', icon: 'products', color: '#6366f1' },
      { title: 'Pending Orders', value: `${pendingCount} Orders`, subtext: 'Awaiting your acceptance', icon: 'orders', color: '#f59e0b' },
      { title: 'Accepted Orders', value: `${acceptedCount} Orders`, subtext: 'Ready to be shipped', icon: 'stock', color: '#0ea5e9' },
      { title: 'Shipped Orders', value: `${shippedCount} Orders`, subtext: 'Fulfilled shipments count', icon: 'shipments', color: '#10b981' }
    ];
  });

  constructor(
    private dashboardService: DashboardService,
    private supplierService: SupplierService,
    private productService: ProductService,
    private categoryService: CategoryService,
    private fb: FormBuilder
  ) {
    this.productForm = this.fb.group({
      sku: ['', Validators.required],
      name: ['', Validators.required],
      description: [''],
      costPrice: [0, [Validators.required, Validators.min(0)]],
      sellingPrice: [0, [Validators.required, Validators.min(0)]],
      stockQuantity: [0, [Validators.required, Validators.min(0)]],
      reorderLevel: [10, [Validators.required, Validators.min(0)]],
      reorderQuantity: [50, [Validators.required, Validators.min(0)]],
      active_status: ['Active', Validators.required],
      category: ['', Validators.required]
      // Note: supplierId is omitted because backend auto-assigns it based on logged-in user
    });
  }

  ngOnInit(): void {
    this.dashboardService.getSupplierDashboard().subscribe({
      next: (data) => {
        this.dashboardData.set(data);
      },
      error: (err) => {
        console.error('Failed to load supplier dashboard stats', err);
      }
    });

    this.loadMyProducts();

    this.supplierService.getMyOrders().subscribe({
      next: (list) => {
        this.dispatchedOrders.set(list);
      },
      error: (err) => {
        console.error('Failed to load supplier purchase orders', err);
      }
    });

    this.categoryService.getAllCategories().subscribe({
      next: (data) => this.categories.set(data),
      error: (err) => console.error('Failed to load categories', err)
    });
  }

  loadMyProducts(): void {
    this.supplierService.getMyProducts().subscribe({
      next: (list) => {
        this.myProductsList.set(list);
      },
      error: (err) => {
        console.error('Failed to load supplier products catalog', err);
      }
    });
  }

  loadCategories(): void {
    this.categoryService.getAllCategories().subscribe({
      next: (data) => this.categories.set(data),
      error: (err) => console.error('Failed to load categories', err)
    });
  }

  openAddModal(): void {
    this.loadCategories(); // Fetch fresh categories when modal opens
    this.productForm.reset({
      costPrice: 0,
      sellingPrice: 0,
      stockQuantity: 0,
      reorderLevel: 10,
      reorderQuantity: 50,
      active_status: 'Active'
    });
    this.showAddModal.set(true);
  }

  closeModal(): void {
    this.showAddModal.set(false);
  }

  submitProduct(): void {
    if (this.productForm.invalid) {
      this.productForm.markAllAsTouched();
      return;
    }

    const dto: ProductRequestDto = this.productForm.getRawValue();

    this.productService.addProduct(dto).subscribe({
      next: () => {
        this.loadMyProducts(); // Refresh product list
        
        // Also refresh dashboard stats to show updated product count
        this.dashboardService.getSupplierDashboard().subscribe({
          next: (data) => this.dashboardData.set(data)
        });

        this.closeModal();
      },
      error: (err) => {
        console.error('Failed to add product', err);
        // Show error message from backend if available
        let errorMsg = 'Failed to add product. ';
        if (err.error) {
          if (typeof err.error === 'string') {
            errorMsg += err.error;
          } else if (err.error.error) {
             errorMsg += err.error.error;
          } else if (Object.keys(err.error).length > 0) {
             // For validation errors mapped to field names
             errorMsg += 'Validation failed: ' + JSON.stringify(err.error);
          }
        }
        alert(errorMsg);
      }
    });
  }
}
