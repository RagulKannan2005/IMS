import { Component, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductService } from '../../../app/services/product';

@Component({
  selector: 'app-products',
  imports: [CommonModule],
  templateUrl: './products.html',
  styleUrl: './products.css',
})
export class Products {
  products: any[] = [];
  isLoading = true;
  errorMessage = '';

  productsService = inject(ProductService);
  cdr = inject(ChangeDetectorRef);

  ngOnInit() {
    console.log('Products component initializing...');
    this.isLoading = true;
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
      }
    });
  }
}
