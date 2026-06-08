import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { WarehouseService } from '../../../core/services/warehouse.service';
import { AuthService } from '../../../core/services/auth.service';
import { UserService } from '../../../core/services/user.service';
import { WarehouseResponseDto, WarehouseRequestDto, UserResponseDto } from '../../../shared/models/interfaces';

@Component({
  selector: 'app-warehouse-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './warehouse-list.html',
  styleUrl: './warehouse-list.css'
})
export class WarehouseListComponent implements OnInit {
  warehouses = signal<WarehouseResponseDto[]>([]);
  loading = signal(false);
  showModal = signal(false);
  isEditMode = signal(false);
  selectedWarehouseId: number | null = null;
  warehouseForm: FormGroup;
  errorMessage = signal<string | null>(null);
  managers = signal<UserResponseDto[]>([]);

  constructor(
    private fb: FormBuilder,
    private warehouseService: WarehouseService,
    private userService: UserService,
    public authService: AuthService
  ) {
    this.warehouseForm = this.fb.group({
      name: ['', Validators.required],
      warehouseCode: ['', Validators.required],
      capacity: ['', [Validators.required, Validators.min(1)]],
      managerName: ['', Validators.required],
      contactNumber: ['', [Validators.required, Validators.pattern(/^\+?[0-9\s-]{10,15}$/)]],
      email: ['', [Validators.required, Validators.email]],
      isActive: ['active', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadWarehouses();
    this.loadManagers();
  }

  loadManagers(): void {
    this.userService.getAllUsers().subscribe({
      next: (users) => {
        const mgrs = users.filter(u => u.role === 'MANAGER');
        this.managers.set(mgrs);
      },
      error: (err) => console.error('Failed to load managers', err)
    });
  }

  onManagerChange(event: any): void {
    const selectedName = event.target.value;
    const manager = this.managers().find(m => m.firstName + ' ' + m.lastName === selectedName || m.username === selectedName);
    if (manager) {
      this.warehouseForm.patchValue({
        managerName: manager.firstName + ' ' + manager.lastName,
        email: manager.email
      });
    }
  }

  loadWarehouses(): void {
    this.loading.set(true);
    this.warehouseService.getAllWarehouses().subscribe({
      next: (list: WarehouseResponseDto[]) => {
        this.warehouses.set(list);
        this.loading.set(false);
      },
      error: (err: any) => {
        console.error('Failed to load warehouses', err);
        this.loading.set(false);
      }
    });
  }

  openCreateModal(): void {
    this.isEditMode.set(false);
    this.selectedWarehouseId = null;
    this.warehouseForm.reset({ isActive: 'active' });
    this.errorMessage.set(null);
    this.showModal.set(true);
  }

  openEditModal(warehouse: WarehouseResponseDto): void {
    this.isEditMode.set(true);
    this.selectedWarehouseId = warehouse.id;
    this.warehouseForm.patchValue({
      name: warehouse.name,
      warehouseCode: warehouse.warehouseCode,
      capacity: warehouse.capacity,
      managerName: warehouse.managerName,
      contactNumber: warehouse.contactNumber,
      email: warehouse.email,
      isActive: warehouse.isActive
    });
    this.errorMessage.set(null);
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
  }

  submit(): void {
    if (this.warehouseForm.invalid) {
      this.warehouseForm.markAllAsTouched();
      return;
    }

    const payload: WarehouseRequestDto = { 
      ...this.warehouseForm.value
    };
    
    this.errorMessage.set(null);

    if (this.isEditMode() && this.selectedWarehouseId) {
      this.warehouseService.updateWarehouse(this.selectedWarehouseId, payload).subscribe({
        next: () => {
          this.loadWarehouses();
          this.closeModal();
        },
        error: (err: any) => {
          console.error('Update failed', err);
          this.errorMessage.set(err.error?.message || 'Failed to update warehouse.');
        }
      });
    } else {
      this.warehouseService.addWarehouse(payload).subscribe({
        next: () => {
          this.loadWarehouses();
          this.closeModal();
        },
        error: (err: any) => {
          console.error('Creation failed', err);
          this.errorMessage.set(err.error?.message || 'Failed to create warehouse.');
        }
      });
    }
  }

  deleteWarehouse(id: number): void {
    if (confirm('Are you sure you want to delete this warehouse? All associated data may be affected.')) {
      this.warehouseService.deleteWarehouse(id).subscribe({
        next: () => {
          this.loadWarehouses();
        },
        error: (err: any) => {
          console.error('Delete failed', err);
          alert('Failed to delete warehouse: ' + (err.error?.message || 'Access Denied or Warehouse has active items.'));
        }
      });
    }
  }
}
