import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { Auth } from '../../../app/services/auth';
import {ProductService} from '../../../app/services/product'

@Component({
  selector: 'app-dashboard-home',
  imports: [CommonModule, RouterLink, RouterOutlet],
  templateUrl: './dashboard-home.html',
  styleUrl: './dashboard-home.css',
})
export class DashboardHome {
  userMenu = false;
  inventoryMenu = false;
  orderMenu = false;

  totalProducts = 0;
  user: any;

  authService = inject(Auth);
  productService = inject(ProductService);
  router = inject(Router);

  ngOnInit() {

    this.user = this.authService.getuser();

    if (this.user?.id) {

      this.productService
        .getProductCountByUser(this.user.id)
        .subscribe({
          next: (count: any) => {
            this.totalProducts = count;
          },
          error: (err) => {
            console.error('Failed to fetch product count', err);
          }
        });

    }

  }

  toggleUsers() {
    this.userMenu = !this.userMenu;
  }

  toggleInventory() {
    this.inventoryMenu = !this.inventoryMenu;
  }

  toggleOrders() {
    this.orderMenu = !this.orderMenu;
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
