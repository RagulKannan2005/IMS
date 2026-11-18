import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { CategoryService } from '../../../core/services/category.service';
import { AuthService } from '../../../core/services/auth.service';
import { CategoryResponseDto, CategoryRequestDto } from '../../../shared/models/interfaces';

@Component({
  selector: 'app-category-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './category-list.html',
  styleUrls: ['./category-list.css']
})
export class CategoryListComponent implements OnInit {
  categories = signal<CategoryResponseDto[]>([]);
  loading = signal<boolean>(false);
  showModal = signal<boolean>(false);
  isEditMode = signal<boolean>(false);
  selectedCategoryId: number | null = null;
  categoryForm: FormGroup;
  errorMessage = signal<string | null>(null);
  searchQuery = signal<string>('');

  filteredCategories = computed(() => {
    const list = this.categories();
    const query = this.searchQuery().toLowerCase().trim();
    if (!query) {
      return list;
    }
    return list.filter(c =>
      (c.name && c.name.toLowerCase().includes(query)) ||
      (c.description && c.description.toLowerCase().includes(query))
    );
  });

  canManageCategories = computed(() => {
    const role = this.authService.userRole();
    return role === 'ADMIN' || role === 'MANAGER';
  });

  constructor(
    private fb: FormBuilder,
    private categoryService: CategoryService,
    public authService: AuthService
  ) {
    this.categoryForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      description: ['', [Validators.required]],
      active_status: ['active', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.loading.set(true);
    this.categoryService.getAllCategories().subscribe({
      next: (list: CategoryResponseDto[]) => {
        this.categories.set(list || []);
        this.loading.set(false);
      },
      error: (err: any) => {
        console.error('Failed to load categories', err);
        this.loading.set(false);
      }
    });
  }

  openCreateModal(): void {
    this.isEditMode.set(false);
    this.selectedCategoryId = null;
    this.categoryForm.reset({
      name: '',
      description: '',
      active_status: 'active'
    });
    this.errorMessage.set(null);
    this.showModal.set(true);
  }

  openEditModal(category: CategoryResponseDto): void {
    this.isEditMode.set(true);
    this.selectedCategoryId = category.id;
    this.categoryForm.patchValue({
      name: category.name,
      description: category.description,
      active_status: category.active_status || 'active'
    });
    this.errorMessage.set(null);
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
    this.categoryForm.reset();
    this.errorMessage.set(null);
  }

  onSubmit(): void {
    if (this.categoryForm.invalid) {
      this.categoryForm.markAllAsTouched();
      return;
    }

    const formValue = this.categoryForm.value;
    const dto: CategoryRequestDto = {
      name: formValue.name?.trim(),
      description: formValue.description?.trim(),
      active_status: formValue.active_status
    };

    this.loading.set(true);
    this.errorMessage.set(null);

    if (this.isEditMode() && this.selectedCategoryId) {
      this.categoryService.updateCategory(this.selectedCategoryId, dto).subscribe({
        next: (updated: CategoryResponseDto) => {
          this.categories.update(list => list.map(c => c.id === updated.id ? updated : c));
          this.closeModal();
          this.loading.set(false);
        },
        error: (err: any) => {
          console.error('Failed to update category', err);
          this.errorMessage.set(err?.error?.message || err?.message || 'Failed to update category.');
          this.loading.set(false);
        }
      });
    } else {
      this.categoryService.createCategory(dto).subscribe({
        next: (created: CategoryResponseDto) => {
          this.categories.update(list => [...list, created]);
          this.closeModal();
          this.loading.set(false);
        },
        error: (err: any) => {
          console.error('Failed to create category', err);
          this.errorMessage.set(err?.error?.message || err?.message || 'Failed to create category.');
          this.loading.set(false);
        }
      });
    }
  }

  deleteCategory(id: number, name: string): void {
    if (!confirm(`Are you sure you want to delete category "${name}"?`)) {
      return;
    }

    this.categoryService.deleteCategory(id).subscribe({
      next: () => {
        this.categories.update(list => list.filter(c => c.id !== id));
      },
      error: (err: any) => {
        console.error('Failed to delete category', err);
        alert(err?.error?.message || 'Failed to delete category.');
      }
    });
  }
}
