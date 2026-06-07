export interface MenuItem {
  label: string;
  route: string;
  icon: string;
}

export const MENU_CONFIG: Record<'ADMIN' | 'MANAGER' | 'STAFF' | 'SUPPLIER', MenuItem[]> = {
  ADMIN: [
    { label: 'Dashboard', route: '/dashboard', icon: 'dashboard' },
    { label: 'Users', route: '/users', icon: 'users' },
    { label: 'Categories', route: '/categories', icon: 'categories' },
    { label: 'Products', route: '/products', icon: 'products' },
    { label: 'Suppliers', route: '/suppliers', icon: 'suppliers' },
    { label: 'Warehouses', route: '/warehouses', icon: 'warehouses' },
    { label: 'Stock Levels', route: '/stock', icon: 'stock' },
    { label: 'Stock Movements', route: '/stock-movements', icon: 'movements' },
    { label: 'Purchase Orders', route: '/purchase-orders', icon: 'orders' },
    { label: 'Reports', route: '/reports', icon: 'reports' },
    { label: 'Settings', route: '/settings', icon: 'settings' }
  ],
  MANAGER: [
    { label: 'Dashboard', route: '/dashboard', icon: 'dashboard' },
    { label: 'Products', route: '/products', icon: 'products' },
    { label: 'Suppliers', route: '/suppliers', icon: 'suppliers' },
    { label: 'Purchase Orders', route: '/purchase-orders', icon: 'orders' },
    { label: 'Warehouses', route: '/warehouses', icon: 'warehouses' },
    { label: 'Stock Levels', route: '/stock', icon: 'stock' },
    { label: 'Reports', route: '/reports', icon: 'reports' }
  ],
  STAFF: [
    { label: 'Dashboard', route: '/dashboard', icon: 'dashboard' },
    { label: 'Products', route: '/products', icon: 'products' },
    { label: 'Stock Levels', route: '/stock', icon: 'stock' },
    { label: 'Stock In', route: '/stock-in', icon: 'stock-in' },
    { label: 'Stock Out', route: '/stock-out', icon: 'stock-out' },
    { label: 'Stock Movements', route: '/stock-movements', icon: 'movements' },
    { label: 'Warehouses', route: '/warehouses', icon: 'warehouses' }
  ],
  SUPPLIER: [
    { label: 'Dashboard', route: '/dashboard', icon: 'dashboard' },
    { label: 'My Products', route: '/supplier/products', icon: 'products' },
    { label: 'My Orders', route: '/supplier/orders', icon: 'orders' },
    { label: 'Shipment Status', route: '/supplier/shipments', icon: 'shipments' },
    { label: 'Profile', route: '/supplier/profile', icon: 'profile' }
  ]
};
