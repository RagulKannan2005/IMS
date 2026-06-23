import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { WarehouseService } from '../../../app/services/warehouse';

@Component({
  selector: 'app-warehouse',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './warehouse.html',
  styleUrl: './warehouse.css',
})
export class Warehouse implements OnInit {
  private warehouseService = inject(WarehouseService);

  warehouses: any[] = [];
  filteredWarehouses: any[] = [];
  
  showAddForm = false;
  showUpdateForm = false;

  newWarehouse = {
    name: '',
    warehouseCode: '',
    capacity: 0,
    managerName: '',
    contactNumber: '',
    email: '',
  };

  updateData = {
    id: null,
    name: '',
    warehouseCode: '',
    capacity: 0,
    managerName: '',
    contactNumber: '',
    email: '',
  };

  ngOnInit() {
    this.loadWarehouses();
  }

  loadWarehouses() {
    this.warehouseService.getAllWarehouses().subscribe({
      next: (data: any[]) => {
        this.warehouses = data;
        this.filteredWarehouses = data;
      },
      error: (err: any) => console.error('Error fetching warehouses:', err),
    });
  }

  searchWarehouse(event: any) {
    const term = event.target.value.toLowerCase();
    this.filteredWarehouses = this.warehouses.filter(
      (w: any) =>
        w.name?.toLowerCase().includes(term) ||
        w.warehouseCode?.toLowerCase().includes(term) ||
        w.managerName?.toLowerCase().includes(term)
    );
  }

  openAddForm() {
    this.showAddForm = true;
    this.newWarehouse = {
      name: '',
      warehouseCode: '',
      capacity: 0,
      managerName: '',
      contactNumber: '',
      email: '',
    };
  }

  closeAddForm() {
    this.showAddForm = false;
  }

  addWarehouse() {
    this.warehouseService.addWarehouse(this.newWarehouse).subscribe({
      next: (res: any) => {
        this.loadWarehouses();
        this.closeAddForm();
      },
      error: (err: any) => console.error('Error adding warehouse:', err),
    });
  }

  openUpdateForm(warehouse: any) {
    this.showUpdateForm = true;
    this.updateData = {
      id: warehouse.id,
      name: warehouse.name,
      warehouseCode: warehouse.warehouseCode,
      capacity: warehouse.capacity,
      managerName: warehouse.managerName,
      contactNumber: warehouse.contactNumber,
      email: warehouse.email,
    };
  }

  closeUpdateForm() {
    this.showUpdateForm = false;
  }

  updateWarehouse() {
    if (this.updateData.id) {
      this.warehouseService.updateWarehouse(this.updateData.id, this.updateData).subscribe({
        next: (res: any) => {
          this.loadWarehouses();
          this.closeUpdateForm();
        },
        error: (err: any) => console.error('Error updating warehouse:', err),
      });
    }
  }

  deleteWarehouse(id: number) {
    if (confirm('Are you sure you want to delete this warehouse?')) {
      this.warehouseService.deleteWarehouse(id).subscribe({
        next: (res: any) => this.loadWarehouses(),
        error: (err: any) => console.error('Error deleting warehouse:', err),
      });
    }
  }
}
