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
  allProducts: any[] = []; // backup for local search filtering
  isLoading = true;
  errorMessage = '';
  newproduct: any = {};
  isEditMode = false;

  productsService = inject(ProductService);
  categoryService = inject(categoryservice);
  cdr = inject(ChangeDetectorRef);
  userService = inject(UserService);

  categories: any[] = [];

  showform = false;
  openform() {
    this.isEditMode = false;
    this.newproduct = {};
    this.showform = true;
  }
  closeform() {
    this.showform = false;
    this.newproduct = {};
    this.isEditMode = false;
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

  editProduct(product: any) {
    this.isEditMode = true;
    this.newproduct = { ...product }; // Shallow copy
    this.showform = true;
  }

  updateProduct() {
    if (this.newproduct.id) {
      this.productsService.updateProduct(this.newproduct.id, this.newproduct).subscribe({
        next: (response: any) => {
          console.log('Product updated successfully', response);
          this.closeform();
          this.ngOnInit(); // Reload products
        },
        error: (err: any) => {
          console.error('Error updating product', err);
          alert('Failed to update the product. Please try again.');
        }
      });
    }
  }

  deleteProduct(id: number) {
    if (confirm('Are you sure you want to delete this product?')) {
      this.productsService.deleteProduct(id).subscribe({
        next: (response: any) => {
          console.log('Product deleted successfully', response);
          this.ngOnInit(); // Reload products
        },
        error: (err: any) => {
          console.error('Error deleting product', err);
          alert('Failed to delete the product.');
        }
      });
    }
  }

  searchProducts(event: any) {
    const term = event.target.value.toLowerCase().trim();
    if (!term) {
      this.products = [...this.allProducts];
      return;
    }
    this.products = this.allProducts.filter((p: any) => 
      p.name?.toLowerCase().includes(term) ||
      p.sku?.toLowerCase().includes(term)
    );
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
          this.allProducts = [...this.products]; // Update backup
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
