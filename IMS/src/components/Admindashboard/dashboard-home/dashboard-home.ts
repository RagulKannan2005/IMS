import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { Auth } from '../../../app/services/auth';
import { ProductService } from '../../../app/services/product';
import { PurchaseOrderService } from '../../../app/services/purchaseorder';
import { SupplierService } from '../../../app/services/supplier';

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
  lowStockAlerts = 0;
  pendingOrders = 0;
  activeSuppliers = 0;

  user: any;

  authService = inject(Auth);
  productService = inject(ProductService);
  purchaseOrderService = inject(PurchaseOrderService);
  supplierService = inject(SupplierService);
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

      this.productService.getAllProducts().subscribe({
        next: (res: any) => {
          let products = [];
          if (res && res.data && Array.isArray(res.data)) products = res.data;
          else if (Array.isArray(res)) products = res;
          
          this.lowStockAlerts = products.filter((p: any) => p.stockQuantity <= p.reorderLevel).length;
        },
        error: (err) => console.error('Failed to fetch products', err)
      });

      this.purchaseOrderService.getAllPurchaseOrders().subscribe({
        next: (orders: any[]) => {
          this.pendingOrders = orders.filter((o: any) => o.orderStatus === 'PENDING').length;
        },
        error: (err) => console.error('Failed to fetch orders', err)
      });

      this.supplierService.getAllSuppliers().subscribe({
        next: (suppliers: any[]) => {
          this.activeSuppliers = suppliers.length; // Assuming all returned suppliers are active
        },
        error: (err) => console.error('Failed to fetch suppliers', err)
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
