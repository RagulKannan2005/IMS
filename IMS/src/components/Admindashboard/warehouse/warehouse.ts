import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { WarehouseService } from '../../../app/services/warehouse';
import { UserService } from '../../../app/services/user';
import { ChangeDetectorRef } from '@angular/core';
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

  userService = inject(UserService);
  cdr = inject(ChangeDetectorRef);

  showAddForm = false;
  showUpdateForm = false;

  newWarehouse: any = {
    name: '',
    warehouseCode: '',
    capacity: 0,
    managerName: '',
    contactNumber: '',
    email: '',
    userId: null,
  };

  updateData: any = {
    id: null,
    name: '',
    warehouseCode: '',
    capacity: 0,
    managerName: '',
    contactNumber: '',
    email: '',
    userId: null,
  };

  ngOnInit() {
    this.loadWarehouses();
  }

  loadWarehouses() {
    this.warehouseService.getAllWarehouses().subscribe({
      next: (data: any[]) => {
        this.warehouses = data;
        this.filteredWarehouses = data;
        this.cdr.detectChanges(); // Force Angular to update the UI
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
        w.managerName?.toLowerCase().includes(term),
    );
  }

  manageremail:any[]=[];

  loademail(){
    this.userService.getAllUsers().subscribe({
      next: (response: any) => {
        if (response && response.data && Array.isArray(response.data)) {
          this.manageremail = response.data.map((u: any) => u.email);
        } else if (Array.isArray(response)) {
          this.manageremail = response.map((u: any) => u.email);
        } else {
          this.manageremail = [];
        }
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('Error fetching emails', err);
        this.cdr.detectChanges();
      }
    });
  }
  managers: any[] = [];
  loadmanager() {
    this.userService.getAllUsers().subscribe({
      next: (response: any) => {
        if (response && response.data && Array.isArray(response.data)) {
          this.managers = response.data.filter((u: any) => u.role === 'MANAGER');
        } else if (Array.isArray(response)) {
          this.managers = response.filter((u: any) => u.role === 'MANAGER');
        } else {
          this.managers = [];
        }
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('Error fetching managers', err);
        this.cdr.detectChanges();
      }
    });
  }
  openAddForm() {
    this.showAddForm = true;
    this.loademail();
    this.loadmanager(); // Make sure managers are loaded when form opens
    this.newWarehouse = {
      name: '',
      warehouseCode: '',
      capacity: 0,
      managerName: '',
      contactNumber: '',
      email: '',
      userId: null,
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
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('Error adding warehouse:', err);
        this.cdr.detectChanges();
      },
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
          this.cdr.detectChanges();
        },
        error: (err: any) => {
          console.error('Error updating warehouse:', err);
          this.cdr.detectChanges();
        },
      });
    }
  }

  deleteWarehouse(id: number) {
    if (confirm('Are you sure you want to delete this warehouse?')) {
      this.warehouseService.deleteWarehouse(id).subscribe({
        next: (res: any) => {
          this.loadWarehouses();
          this.cdr.detectChanges();
        },
        error: (err: any) => {
          console.error('Error deleting warehouse:', err);
          this.cdr.detectChanges();
        },
      });
    }
  }
}
