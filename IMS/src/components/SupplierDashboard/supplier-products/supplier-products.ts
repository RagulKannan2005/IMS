import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SupplierProductService } from '../../../app/services/supplier-product';
import { categoryservice } from '../../../app/services/category';

@Component({
  selector: 'app-supplier-products',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './supplier-products.html',
  styleUrl: './supplier-products.css',
})
export class SupplierProducts implements OnInit {
  products: any[] = [];
  categories: any[] = [
    { name: "Electronics" },
    { name: "Books" },
    { name: "Stationery" },
    { name: "Furniture" },
    { name: "others" }
  ];
  showform = false;
  productService = inject(SupplierProductService);
  categoryService = inject(categoryservice);
  cdr = inject(ChangeDetectorRef);

  newProduct = {
    sku: '',
    name: '',
    description: '',
    costPrice: null,
    availableQuantity: 0,
    active_status: 'Active',
    category: '',
  };

  ngOnInit() {
    this.loadProducts();
    this.loadCategories();
  }

  loadCategories() {
    this.categoryService.getDefaultCategories().subscribe({
      next: (data) => {
        this.categories = data;
      },
      error: (err) => {
        console.error('Error fetching categories', err);
      },
    });
  }

  loadProducts() {
    this.productService.getAllProducts().subscribe({
      next: (response: any) => {
        if (response && response.data && Array.isArray(response.data)) {
          this.products = response.data;
        } else if (response && response.value && Array.isArray(response.value)) {
          this.products = response.value;
        } else if (Array.isArray(response)) {
          this.products = response;
        } else {
          this.products = [];
        }
        this.cdr.detectChanges(); // Force view update
      },
      error: (err) => {
        console.error('Error fetching products', err);
        alert('Failed to load products');
      },
    });
  }

  openform() {
    this.showform = true;
  }

  closeform() {
    this.showform = false;
    this.resetForm();
  }

  resetForm() {
    this.newProduct = {
      sku: '',
      name: '',
      description: '',
      costPrice: null,
      availableQuantity: 0,
      active_status: 'Active',
      category: '',
    };
  }

  submitForm() {
    this.productService.addProduct(this.newProduct).subscribe({
      next: (response) => {
        alert('Product added successfully!');
        this.closeform();
        this.loadProducts(); // Refresh the list
      },
      error: (err) => {
        console.error('Error adding product', err);
        alert('Failed to add product. Please check the fields and try again.');
      },
    });
  }
}
