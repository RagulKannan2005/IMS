import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { UserService } from '../../../core/services/user.service';
import { AuthService } from '../../../core/services/auth.service';
import { UserResponseDto } from '../../../shared/models/interfaces';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './user-list.html',
  styleUrl: './user-list.css'
})
export class UserListComponent implements OnInit {
  users = signal<UserResponseDto[]>([]);
  loading = signal(false);
  showModal = signal(false);
  isEditMode = signal(false);
  selectedUserId: number | null = null;
  userForm: FormGroup;
  errorMessage = signal<string | null>(null);

  // Available roles selection based on current user role
  availableRoles = computed(() => {
    const role = this.authService.userRole();
    if (role === 'MANAGER') {
      return ['STAFF'];
    }
    return ['ADMIN', 'MANAGER', 'STAFF', 'SUPPLIER'];
  });

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    public authService: AuthService
  ) {
    this.userForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(4)]],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^\+?[0-9\s-]{10,15}$/)]],
      role: ['STAFF', Validators.required],
      supplierId: [null]
    });

    // Enforce role auto-locking for Manager creation
    if (this.authService.userRole() === 'MANAGER') {
      this.userForm.patchValue({ role: 'STAFF' });
    }
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading.set(true);
    this.userService.getAllUsers().subscribe({
      next: (list) => {
        this.users.set(list);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Failed to load users', err);
        this.loading.set(false);
      }
    });
  }

  openCreateModal(): void {
    this.isEditMode.set(false);
    this.selectedUserId = null;
    this.userForm.reset();
    
    // Set standard role boundaries
    if (this.authService.userRole() === 'MANAGER') {
      this.userForm.patchValue({ role: 'STAFF' });
    } else {
      this.userForm.patchValue({ role: 'STAFF' });
    }
    
    this.userForm.get('password')?.setValidators([Validators.required, Validators.minLength(4)]);
    this.userForm.get('password')?.updateValueAndValidity();
    this.errorMessage.set(null);
    this.showModal.set(true);
  }

  openEditModal(user: UserResponseDto): void {
    this.isEditMode.set(true);
    this.selectedUserId = user.id;
    this.userForm.patchValue({
      username: user.username,
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      phoneNumber: user.phoneNumber,
      role: user.role,
      supplierId: user.supplierId,
      password: '' // empty for edit
    });

    // Password is not required when editing
    this.userForm.get('password')?.clearValidators();
    this.userForm.get('password')?.setValidators([Validators.minLength(4)]);
    this.userForm.get('password')?.updateValueAndValidity();
    
    this.errorMessage.set(null);
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
  }

  canModifyUser(user: UserResponseDto): boolean {
    const actorRole = this.authService.userRole();
    const actorId = this.authService.currentUser()?.email; // our security mapping stores email as principal username
    
    if (actorRole === 'ADMIN') {
      return true;
    }
    if (actorRole === 'MANAGER') {
      // Manager can edit only staff they created (which has createdById matching their own user id)
      // Since createdByUsername is their email or username, we can match
      const currentUsername = this.authService.currentUser()?.username;
      return user.role === 'STAFF' && (user.createdByUsername === currentUsername || user.createdById === this.authService.currentUser()?.email as any);
    }
    return false;
  }

  submit(): void {
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      return;
    }

    const payload = { ...this.userForm.value };
    
    // Clear password if blank during editing
    if (this.isEditMode() && !payload.password) {
      delete payload.password;
    }

    this.errorMessage.set(null);

    if (this.isEditMode() && this.selectedUserId) {
      this.userService.updateUser(this.selectedUserId, payload).subscribe({
        next: () => {
          this.loadUsers();
          this.closeModal();
        },
        error: (err) => {
          console.error('Update failed', err);
          this.errorMessage.set(err.error?.message || 'Failed to update user. Email or Username might already exist.');
        }
      });
    } else {
      this.userService.createUser(payload).subscribe({
        next: () => {
          this.loadUsers();
          this.closeModal();
        },
        error: (err) => {
          console.error('Creation failed', err);
          this.errorMessage.set(err.error?.message || 'Failed to create user. Email or Username might already exist.');
        }
      });
    }
  }

  deleteUser(id: number): void {
    if (confirm('Are you sure you want to delete this user account?')) {
      this.userService.deleteUser(id).subscribe({
        next: () => {
          this.loadUsers();
        },
        error: (err) => {
          console.error('Delete failed', err);
          alert('Failed to delete user: ' + (err.error?.message || 'Access Denied'));
        }
      });
    }
  }
}
