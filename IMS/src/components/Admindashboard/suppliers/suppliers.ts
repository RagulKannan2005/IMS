import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SupplierService } from '../../../app/services/supplier';

@Component({
  selector: 'app-suppliers',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './suppliers.html',
  styleUrl: './suppliers.css',
})
export class Suppliers implements OnInit {
  private supplierService = inject(SupplierService);
  private cdr = inject(ChangeDetectorRef);

  suppliers: any[] = [];
  allSuppliers: any[] = []; // Backup for local filtering
  isLoading = true;
  errorMessage = '';

  showEditForm = false;
  selectedSupplier: any = {
    id: null,
    supplierName: '',
    contactPerson: '',
    supplier_email: '',
    supplierPhone: '',
    address: '',
    status: true
  };

  ngOnInit() {
    this.loadSuppliers();
  }

  loadSuppliers() {
    this.isLoading = true;
    this.supplierService.getAllSuppliers().subscribe({
      next: (response: any) => {
        let suppliersList = [];
        if (response && response.data && Array.isArray(response.data)) {
          suppliersList = response.data;
        } else if (response && response.value && Array.isArray(response.value)) {
          suppliersList = response.value;
        } else if (Array.isArray(response)) {
          suppliersList = response;
        }

        this.suppliers = suppliersList;
        this.allSuppliers = [...suppliersList];
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('Failed to load suppliers:', err);
        this.errorMessage = 'Failed to load suppliers. Make sure backend is running.';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  searchSupplier(event: any) {
    const term = event.target.value.toLowerCase().trim();
    if (!term) {
      this.suppliers = [...this.allSuppliers];
      return;
    }
    this.suppliers = this.allSuppliers.filter(s => 
      s.supplierName?.toLowerCase().includes(term) ||
      s.contactPerson?.toLowerCase().includes(term) ||
      s.supplier_email?.toLowerCase().includes(term)
    );
  }

  openEditForm(supplier: any) {
    this.selectedSupplier = { ...supplier };
    this.showEditForm = true;
  }

  closeEditForm() {
    this.showEditForm = false;
    this.selectedSupplier = {
      id: null,
      supplierName: '',
      contactPerson: '',
      supplier_email: '',
      supplierPhone: '',
      address: '',
      status: true
    };
  }

  updateSupplier() {
    if (this.selectedSupplier.id) {
      this.supplierService.updateSupplier(this.selectedSupplier.id, this.selectedSupplier).subscribe({
        next: (res: any) => {
          alert('Supplier updated successfully!');
          this.closeEditForm();
          this.loadSuppliers();
        },
        error: (err: any) => {
          console.error('Error updating supplier:', err);
          alert('Failed to update supplier. Please try again.');
        }
      });
    }
  }

  deleteSupplier(id: number) {
    if (confirm('Are you sure you want to delete this supplier profile?')) {
      this.supplierService.deleteSupplier(id).subscribe({
        next: () => {
          alert('Supplier profile deleted.');
          this.loadSuppliers();
        },
        error: (err: any) => {
          console.error('Error deleting supplier:', err);
          alert('Failed to delete supplier.');
        }
      });
    }
  }
}
