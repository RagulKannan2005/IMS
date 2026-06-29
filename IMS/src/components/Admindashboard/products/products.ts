import { Component, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../../app/services/product';
import { categoryservice } from '../../../app/services/category';
import { UserService } from '../../../app/services/user';
@Component({
  selector: 'app-products',
  imports: [CommonModule, FormsModule],
  templateUrl: './products.html',
  styleUrl: './products.css',
})
export class Products {
  products: any[] = [];
  isLoading = true;
  errorMessage = '';
  newproduct: any = {};

  productsService = inject(ProductService);
  categoryService = inject(categoryservice);
  cdr = inject(ChangeDetectorRef);
  userService = inject(UserService);

  categories: any[] = [];

  showform = false;
  openform() {
    this.showform = true;
  }
  closeform() {
    this.showform = false;
  }
  addProduct() {
    this.productsService.addProduct(this.newproduct).subscribe({
      next: (response: any) => {
        console.log('Product added successfully', response);
        this.closeform();
        this.ngOnInit(); // Reload products
        this.newproduct = {}; // Reset form data
      },
      error: (err: any) => {
        console.error('Error adding product', err);
        alert('Failed to save the product. Please try again.');
      },
    });
  }

  ngOnInit() {
    console.log('Products component initializing...');
    this.isLoading = true;
    this.loadCategories();
    this.productsService.getAllProducts().subscribe({
      next: (response: any) => {
        console.log('API Request succeeded. Raw response:', response);
        try {
          // In case the response is wrapped inside { data: [...] } or { value: [...] }
          if (response && response.data && Array.isArray(response.data)) {
            this.products = response.data;
          } else if (response && response.value && Array.isArray(response.value)) {
            this.products = response.value;
          } else if (Array.isArray(response)) {
            this.products = response;
          } else {
            console.error('Unexpected response format:', response);
            this.products = [];
          }
          console.log('Processed products array:', this.products);
          this.isLoading = false;
          this.cdr.detectChanges(); // Force view update
        } catch (e) {
          console.error('Error processing response:', e);
          this.errorMessage = 'Failed to process product data.';
          this.isLoading = false;
          this.cdr.detectChanges(); // Force view update
        }
      },
      error: (err) => {
        console.error('Failed to fetch products', err);
        this.errorMessage = 'Failed to load products. Please try again.';
        this.isLoading = false;
        this.cdr.detectChanges(); // Force view update
      },
    });
  }
  

  loadCategories() {
    this.categoryService.getAllCategories().subscribe({
      next: (response: any) => {
        if (response && response.data && Array.isArray(response.data)) {
          this.categories = response.data;
        } else if (response && response.value && Array.isArray(response.value)) {
          this.categories = response.value;
        } else if (Array.isArray(response)) {
          this.categories = response;
        } else {
          this.categories = [];
        }
      },
      error: (err) => {
        console.error('Failed to load categories', err);
      },
    });
  }
}
