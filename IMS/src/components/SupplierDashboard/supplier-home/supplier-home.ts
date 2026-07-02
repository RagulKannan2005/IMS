import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { Auth } from '../../../app/services/auth';
import { SupplierService } from '../../../app/services/supplier';

@Component({
  selector: 'app-supplier-home',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterOutlet],
  templateUrl: './supplier-home.html',
  styleUrl: './supplier-home.css',
})
export class SupplierHome implements OnInit {
  user: any;
  stats: any = {
    pendingOrders: 0,
    inTransitOrders: 0,
    totalProducts: 0,
    monthlyRevenue: 0
  };
  authService = inject(Auth);
  router = inject(Router);
  supplierService = inject(SupplierService);

  ngOnInit() {
    this.user = this.authService.getuser();
    this.loadStats();
  }

  loadStats() {
    this.supplierService.getDashboardStats().subscribe({
      next: (data) => {
        if (data) {
          this.stats = data;
        }
      },
      error: (err) => {
        console.error('Error fetching dashboard stats', err);
      }
    });
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
