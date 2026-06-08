export interface AuthRequest {
  email: string;
  password?: string;
}

export interface AuthResponse {
  token: string;
  username: string;
  role: 'ADMIN' | 'MANAGER' | 'STAFF' | 'SUPPLIER';
  supplierId: number | null;
  firstName: string;
  lastName: string;
  email: string;
}

export interface UserRequestDto {
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  password?: string;
  phoneNumber: string;
  role: 'ADMIN' | 'MANAGER' | 'STAFF' | 'SUPPLIER';
  supplierId?: number | null;
}

export interface UserResponseDto {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  role: 'ADMIN' | 'MANAGER' | 'STAFF' | 'SUPPLIER';
  supplierId: number | null;
  createdById?: number | null;
  createdByUsername?: string | null;
}

export interface CategoryRequestDto {
  name: string;
  description: string;
  active_status: 'active' | 'inactive';
}

export interface CategoryResponseDto {
  id: number;
  name: string;
  description: string;
  active_status: string;
}

export interface ProductRequestDto {
  sku: string;
  name: string;
  description: string;
  costPrice: number;
  sellingPrice: number;
  stockQuantity: number;
  reorderLevel: number;
  reorderQuantity: number;
  active_status: string; // 'active' or 'inactive'
  category: string; // Category Name
  supplierId?: number | null;
}

export interface ProductResponseDto {
  id: number;
  sku: string;
  name: string;
  description: string;
  costPrice: number;
  sellingPrice: number;
  stockQuantity: number;
  reorderLevel: number;
  reorderQuantity: number;
  isActive: boolean;
  supplierId: number | null;
  supplierName: string | null;
  categoryName?: string;
}

export interface PriceUpdateRequestDto {
  costPrice: number;
  sellingPrice: number;
}

export interface StockAdjustmentRequest {
  adjustmentQuantity: number;
  reason: string;
}

export interface SupplierRequestDto {
  supplierName: string;
  contactPerson: string;
  supplierEmail: string;
  supplierPhone: string;
  address: string;
  status: boolean;
  userId?: number | null;
}

export interface SupplierResponseDto {
  id: number;
  supplierName: string;
  contactPerson: string;
  supplierEmail: string;
  supplierPhone: string;
  address: string;
  status: boolean;
  userId: number | null;
}

export interface WarehouseRequestDto {
  name: string;
  warehouseCode: string;
  capacity: number;
  managerName: string;
  contactNumber: string;
  email: string;
}

export interface WarehouseResponseDto {
  id: number;
  name: string;
  warehouseCode: string;
  capacity: number;
  managerName: string;
  contactNumber: string;
  email: string;
  isActive: string; // 'active' or 'inactive'
  createdDate: string; // ISO date string
  updatedDate: string; // ISO date string
}

export interface StockRequestDto {
  productId: number;
  warehouseId: number;
  quantityOnHand: number;
}

export interface StockResponseDto {
  id: number;
  productName: string;
  warehouseName: string;
  quantityOnHand: number;
}

export interface StockTransferRequestDto {
  fromWarehouseId: number;
  toWarehouseId: number;
  productId: number;
  quantityOnHand: number;
}

export interface StockMovementRequestDto {
  productId: number;
  warehouseId: number;
  movementType: 'IN' | 'OUT' | 'ADJUSTMENT' | 'TRANSFER';
  quantity: number;
  referenceNo?: string;
  performedBy?: number;
  remarks?: string;
  toWarehouseId?: number | null;
}

export interface StockMovementResponseDto {
  id: number;
  productId: number;
  productName: string;
  warehouseId: number;
  warehouseName: string;
  movementType: 'IN' | 'OUT' | 'ADJUSTMENT' | 'TRANSFER';
  quantity: number;
  referenceNo: string;
  performedById: number;
  performedByUsername: string;
  remarks: string;
  createdAt: string; // ISO datetime
}

export interface PurchaseOrderRequestDto {
  poNumber: string;
  supplierId: number;
  createdBy: number;
  totalAmount: number;
  status: 'PENDING' | 'APPROVED' | 'SHIPPED' | 'RECEIVED' | 'CANCELLED';
  orderedAt?: string; // ISO date
  expectedDeliveryDate: string; // ISO date
  remarks?: string;
}

export interface PurchaseOrderResponseDto {
  id: number;
  poNumber: string;
  supplierName: string;
  createdBy: string; // username or name of creator
  totalAmount: number;
  status: string;
  orderedAt: string; // ISO date
  expectedDeliveryDate: string; // ISO date
  remarks: string;
}

export interface PurchaseOrderItemRequestDto {
  purchaseOrderId: number;
  productId: number;
  quantityOrdered: number;
  quantityReceived: number;
  unitCost: number;
}

export interface PurchaseOrderItemResponseDto {
  id: number;
  purchaseOrderId: number;
  poNumber: string;
  productId: number;
  productName: string;
  quantityOrdered: number;
  quantityReceived: number;
  unitCost: number;
  totalCost: number;
  createdAt: string;
  updatedAt: string;
}
