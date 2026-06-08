import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SupplierService } from '../../../core/services/supplier.service';
import { ProductResponseDto } from '../../../shared/models/interfaces';

@Component({
  selector: 'app-supplier-product-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './supplier-product-list.html',
  styleUrl: './supplier-product-list.css'
})
export class SupplierProductListComponent implements OnInit {
  myProductsList = signal<ProductResponseDto[]>([]);

  constructor(private supplierService: SupplierService) {}

  ngOnInit(): void {
    this.loadMyProducts();
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
}
