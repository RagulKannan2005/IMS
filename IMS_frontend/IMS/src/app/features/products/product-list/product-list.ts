import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProductService } from '../../../core/services/product.service';
import { CategoryService } from '../../../core/services/category.service';
import { SupplierService } from '../../../core/services/supplier.service';
import { ProductResponseDto, CategoryResponseDto, SupplierResponseDto, ProductRequestDto } from '../../../shared/models/interfaces';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './product-list.html',
  styleUrls: ['./product-list.css']
})
export class ProductListComponent implements OnInit {
  products = signal<ProductResponseDto[]>([]);
  categories = signal<CategoryResponseDto[]>([]);
  suppliers = signal<SupplierResponseDto[]>([]);
  
  showModal = signal<boolean>(false);
  isEditing = signal<boolean>(false);
  editingProductId = signal<number | null>(null);
  
  productForm: FormGroup;

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private supplierService: SupplierService,
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
      category: ['', Validators.required],
      supplierId: [null]
    });
  }

  ngOnInit(): void {
    this.loadProducts();
    this.loadCategories();
    this.loadSuppliers();
  }

  loadProducts(): void {
    this.productService.getAllProducts().subscribe({
      next: (data) => this.products.set(data),
      error: (err) => console.error('Failed to load products', err)
    });
  }

  loadCategories(): void {
    this.categoryService.getAllCategories().subscribe({
      next: (data) => this.categories.set(data),
      error: (err) => console.error('Failed to load categories', err)
    });
  }

  loadSuppliers(): void {
    this.supplierService.getAllSuppliers().subscribe({
      next: (data) => this.suppliers.set(data.filter(s => s.status)),
      error: (err) => console.error('Failed to load suppliers', err)
    });
  }

  openAddModal(): void {
    this.isEditing.set(false);
    this.editingProductId.set(null);
    this.productForm.reset({
      costPrice: 0,
      sellingPrice: 0,
      stockQuantity: 0,
      reorderLevel: 10,
      reorderQuantity: 50,
      active_status: 'Active'
    });
    this.showModal.set(true);
  }

  openEditModal(product: ProductResponseDto): void {
    this.isEditing.set(true);
    this.editingProductId.set(product.id);
    this.productForm.patchValue({
      sku: product.sku,
      name: product.name,
      description: product.description,
      costPrice: product.costPrice,
      sellingPrice: product.sellingPrice,
      stockQuantity: product.stockQuantity,
      reorderLevel: product.reorderLevel,
      reorderQuantity: product.reorderQuantity,
      active_status: product.isActive ? 'Active' : 'Inactive',
      category: product.categoryName || '',
      supplierId: product.supplierId
    });
    
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
  }

  saveProduct(): void {
    if (this.productForm.invalid) {
      this.productForm.markAllAsTouched();
      return;
    }

    const dto: ProductRequestDto = this.productForm.getRawValue();

    if (this.isEditing() && this.editingProductId()) {
      this.productService.updateProduct(this.editingProductId()!, dto).subscribe({
        next: () => {
          this.loadProducts();
          this.closeModal();
        },
        error: (err) => console.error('Failed to update product', err)
      });
    } else {
      this.productService.addProduct(dto).subscribe({
        next: () => {
          this.loadProducts();
          this.closeModal();
        },
        error: (err) => console.error('Failed to add product', err)
      });
    }
  }

  deleteProduct(id: number): void {
    if (confirm('Are you sure you want to delete this product?')) {
      this.productService.deleteProduct(id).subscribe({
        next: () => {
          this.loadProducts();
        },
        error: (err) => console.error('Failed to delete product', err)
      });
    }
  }
}
