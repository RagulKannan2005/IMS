import { Component, OnInit } from '@angular/core';
import { Routes, ActivatedRoute } from '@angular/router';
import { Login } from './features/auth/login/login';
import { Register } from './features/auth/register/register';
import { Maindashboard } from './features/dashboard/maindashboard/maindashboard';
import { MainLayout } from './shared/components/layout/main-layout';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';
import { ReceiveOrder } from './features/purchase-orders/receive-order/receive-order';
import { ShipOrder } from './features/purchase-orders/ship-order/ship-order';
import { AccessDenied } from './features/auth/access-denied/access-denied';
import { UserListComponent } from './features/users/user-list/user-list';

// Simple reusable placeholder component to keep routes valid and testable
@Component({
  selector: 'app-placeholder',
  standalone: true,
  template: `
    <div class="dashboard-panel fade-in">
      <div class="panel-header">
        <h2 class="panel-title" style="font-size: 20px;">{{ title }} Page</h2>
        <span class="status-pill success">Active</span>
      </div>
      <p style="color: var(--text-secondary); font-size: 14px; margin-top: 12px; line-height: 1.6;">
        This page represents the <strong>{{ title }}</strong> module. 
        It has been successfully secured using Angular's <code>RoleGuard</code>. 
        Only users with the authorized role permissions can load this route.
      </p>
    </div>
  `
})
export class Placeholder implements OnInit {
  title = '';
  constructor(private route: ActivatedRoute) {}
  ngOnInit() {
    this.title = this.route.snapshot.data['title'] || 'Inventory';
  }
}

export const routes: Routes = [
  {
    path: 'login',
    component: Login
  },
  {
    path: 'register',
    component: Register
  },
  {
    path: '',
    component: MainLayout,
    canActivate: [authGuard],
    children: [
      { 
        path: 'dashboard', 
        component: Maindashboard 
      },
      { 
        path: 'users', 
        component: UserListComponent, 
        canActivate: [roleGuard], 
        data: { roles: ['ADMIN', 'MANAGER'], title: 'User Management' } 
      },
      { 
        path: 'categories', 
        component: Placeholder, 
        canActivate: [roleGuard], 
        data: { roles: ['ADMIN'], title: 'Product Categories' } 
      },
      { 
        path: 'products', 
        component: Placeholder, 
        canActivate: [roleGuard], 
        data: { roles: ['ADMIN', 'MANAGER', 'STAFF'], title: 'Product Catalog' } 
      },
      { 
        path: 'suppliers', 
        component: Placeholder, 
        canActivate: [roleGuard], 
        data: { roles: ['ADMIN', 'MANAGER'], title: 'Suppliers Directory' } 
      },
      { 
        path: 'warehouses', 
        component: Placeholder, 
        canActivate: [roleGuard], 
        data: { roles: ['ADMIN', 'MANAGER', 'STAFF'], title: 'Warehouse Locations' } 
      },
      { 
        path: 'stock', 
        component: Placeholder, 
        canActivate: [roleGuard], 
        data: { roles: ['ADMIN', 'MANAGER', 'STAFF'], title: 'Stock Levels' } 
      },
      { 
        path: 'stock-in', 
        component: Placeholder, 
        canActivate: [roleGuard], 
        data: { roles: ['STAFF'], title: 'Stock Inbound Operations' } 
      },
      { 
        path: 'stock-out', 
        component: Placeholder, 
        canActivate: [roleGuard], 
        data: { roles: ['STAFF'], title: 'Stock Outbound Operations' } 
      },
      { 
        path: 'stock-movements', 
        component: Placeholder, 
        canActivate: [roleGuard], 
        data: { roles: ['ADMIN', 'STAFF'], title: 'Stock Movement Logs' } 
      },
      { 
        path: 'purchase-orders', 
        component: Placeholder, 
        canActivate: [roleGuard], 
        data: { roles: ['ADMIN', 'MANAGER'], title: 'Purchase Orders' } 
      },
      { 
        path: 'purchase-orders/receive/:id', 
        component: ReceiveOrder, 
        canActivate: [roleGuard], 
        data: { roles: ['ADMIN', 'MANAGER'], title: 'Receive Purchase Order' } 
      },
      { 
        path: 'reports', 
        component: Placeholder, 
        canActivate: [roleGuard], 
        data: { roles: ['ADMIN', 'MANAGER'], title: 'Reports & Analytics' } 
      },
      { 
        path: 'settings', 
        component: Placeholder, 
        canActivate: [roleGuard], 
        data: { roles: ['ADMIN'], title: 'Settings Configuration' } 
      },
      // Supplier Portal Routes
      { 
        path: 'supplier/products', 
        component: Placeholder, 
        canActivate: [roleGuard], 
        data: { roles: ['SUPPLIER'], title: 'My Supplied Products' } 
      },
      { 
        path: 'supplier/orders', 
        component: Placeholder, 
        canActivate: [roleGuard], 
        data: { roles: ['SUPPLIER'], title: 'My Purchase Orders' } 
      },
      { 
        path: 'supplier/orders/ship/:id', 
        component: ShipOrder, 
        canActivate: [roleGuard], 
        data: { roles: ['SUPPLIER'], title: 'Ship Order' } 
      },
      { 
        path: 'supplier/shipments', 
        component: Placeholder, 
        canActivate: [roleGuard], 
        data: { roles: ['SUPPLIER'], title: 'Shipment Status Updates' } 
      },
      { 
        path: 'supplier/profile', 
        component: Placeholder, 
        canActivate: [roleGuard], 
        data: { roles: ['SUPPLIER'], title: 'Supplier Profile Settings' } 
      },
      {
        path: '403',
        component: AccessDenied
      },
      { 
        path: '', 
        redirectTo: 'dashboard', 
        pathMatch: 'full' 
      }
    ]
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];
