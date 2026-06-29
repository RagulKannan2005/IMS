import { Routes } from '@angular/router';
import { Register } from '../components/register/register';
import { Login } from '../components/login/login';
import { Home } from '../components/home/home';
import { DashboardHome } from '../components/Admindashboard/dashboard-home/dashboard-home';
import { Products } from '../components/Admindashboard/products/products';
import { adminGuard } from './guards/admin.guard';
import { Categories } from '../components/Admindashboard/categories/categories';
import { Stocks } from '../components/Admindashboard/stocks/stocks';
import { Warehouse } from '../components/Admindashboard/warehouse/warehouse';
import { Users } from '../components/Admindashboard/users/users';
// import { PurchaseOrder } from '../components/Admindashboard/purchase-order/purchase-order';
import { PurchaseOrder } from '../components/Admindashboard/purchase-order/purchase-order';
export const routes: Routes = [
  { path: '', redirectTo: 'register', pathMatch: 'full' },
  { path: 'register', component: Register },
  { path: 'login', component: Login },
  { path: 'home', component: Home },
  {
    path: 'admin',
    component: DashboardHome,
    canActivate: [adminGuard],
    children: [
      { path: 'products', component: Products },
      { path: 'categories', component: Categories },
      { path: 'stocks', component: Stocks },
      { path: 'warehouse', component: Warehouse },
      { path: 'users', component: Users },
      { path: 'purchaseOrders', component: PurchaseOrder },
    ],
  },
];
