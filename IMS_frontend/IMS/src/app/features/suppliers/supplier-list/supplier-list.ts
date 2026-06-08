import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { SupplierService } from '../../../core/services/supplier.service';
import { AuthService } from '../../../core/services/auth.service';
import { SupplierResponseDto, SupplierRequestDto } from '../../../shared/models/interfaces';

@Component({
  selector: 'app-supplier-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './supplier-list.html',
  styleUrl: './supplier-list.css'
})
export class SupplierListComponent implements OnInit {
  suppliers = signal<SupplierResponseDto[]>([]);
  loading = signal(false);
  showModal = signal(false);
  isEditMode = signal(false);
  selectedSupplierId: number | null = null;
  supplierForm: FormGroup;
  errorMessage = signal<string | null>(null);

  constructor(
    private fb: FormBuilder,
    private supplierService: SupplierService,
    public authService: AuthService
  ) {
    this.supplierForm = this.fb.group({
      supplierName: ['', Validators.required],
      contactPerson: ['', Validators.required],
      supplierEmail: ['', [Validators.required, Validators.email]],
      supplierPhone: ['', [Validators.required, Validators.pattern(/^\+?[0-9\s-]{10,15}$/)]],
      address: ['', Validators.required],
      status: [true, Validators.required],
      userId: [null]
    });
  }

  ngOnInit(): void {
    this.loadSuppliers();
  }

  loadSuppliers(): void {
    this.loading.set(true);
    this.supplierService.getAllSuppliers().subscribe({
      next: (list: SupplierResponseDto[]) => {
        this.suppliers.set(list);
        this.loading.set(false);
      },
      error: (err: any) => {
        console.error('Failed to load suppliers', err);
        this.loading.set(false);
      }
    });
  }

  openCreateModal(): void {
    this.isEditMode.set(false);
    this.selectedSupplierId = null;
    this.supplierForm.reset({ status: true });
    this.errorMessage.set(null);
    this.showModal.set(true);
  }

  openEditModal(supplier: SupplierResponseDto): void {
    this.isEditMode.set(true);
    this.selectedSupplierId = supplier.id;
    this.supplierForm.patchValue({
      supplierName: supplier.supplierName,
      contactPerson: supplier.contactPerson,
      supplierEmail: supplier.supplierEmail,
      supplierPhone: supplier.supplierPhone,
      address: supplier.address,
      status: supplier.status,
      userId: supplier.userId
    });
    this.errorMessage.set(null);
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
  }

  submit(): void {
    if (this.supplierForm.invalid) {
      this.supplierForm.markAllAsTouched();
      return;
    }

    const payload: SupplierRequestDto = { ...this.supplierForm.value };
    this.errorMessage.set(null);

    if (this.isEditMode() && this.selectedSupplierId) {
      this.supplierService.updateSupplier(this.selectedSupplierId, payload).subscribe({
        next: () => {
          this.loadSuppliers();
          this.closeModal();
        },
        error: (err: any) => {
          console.error('Update failed', err);
          this.errorMessage.set(err.error?.message || 'Failed to update supplier.');
        }
      });
    } else {
      this.supplierService.addSupplier(payload).subscribe({
        next: () => {
          this.loadSuppliers();
          this.closeModal();
        },
        error: (err: any) => {
          console.error('Creation failed', err);
          this.errorMessage.set(err.error?.message || 'Failed to create supplier.');
        }
      });
    }
  }

  deleteSupplier(id: number): void {
    if (confirm('Are you sure you want to delete this supplier? All associated data may be affected.')) {
      this.supplierService.deleteSupplier(id).subscribe({
        next: () => {
          this.loadSuppliers();
        },
        error: (err: any) => {
          console.error('Delete failed', err);
          alert('Failed to delete supplier: ' + (err.error?.message || 'Access Denied or Supplier has active orders.'));
        }
      });
    }
  }
}
