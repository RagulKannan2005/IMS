import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../../app/services/user';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './users.html',
  styleUrl: './users.css',
})
export class Users implements OnInit {
  private userService = inject(UserService);
  private cdr = inject(ChangeDetectorRef);

  users: any[] = [];
  filteredUsers: any[] = [];
  showform = false;
  isEditMode = false;

  // Variables for Manager dropdown
  managers: any[] = [];
  selectedManagerId: number | null = null;
  currentAdminid: number = 1; // Testing with admin ID 1

  newUser: any = {
    username: '',
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    phone_number: '',
    role: 'STAFF',
  };

  ngOnInit() {
    this.loadUsers();
    this.loadManagers();
  }

  loadUsers() {
    this.userService.getAllUsers().subscribe({
      next: (response: any) => {
        let usersData = response;
        if (response && response.data && Array.isArray(response.data)) {
          usersData = response.data;
        } else if (response && response.value && Array.isArray(response.value)) {
          usersData = response.value;
        } else if (!Array.isArray(response)) {
          usersData = [];
        }
        
        this.users = usersData;
        this.filteredUsers = usersData;
        this.cdr.detectChanges(); // Force Angular to update the UI
      },
      error: (err: any) => console.error('Error fetching users:', err),
    });
  }

  loadManagers() {
    this.userService.getManagersByAdminId(this.currentAdminid).subscribe({
      next: (data: any[]) => {
        this.managers = data;
      },
      error: (err: any) => console.error('Error fetching managers:', err),
    });
  }

  searchUser(event: any) {
    const term = event.target.value.toLowerCase();
    this.filteredUsers = this.users.filter(
      (u: any) =>
        u.firstName?.toLowerCase().includes(term) ||
        u.lastName?.toLowerCase().includes(term) ||
        u.email?.toLowerCase().includes(term) ||
        u.role?.toLowerCase().includes(term),
    );
  }

  openform() {
    this.isEditMode = false;
    this.newUser = {
      username: '',
      firstName: '',
      lastName: '',
      email: '',
      password: '',
      phone_number: '',
      role: 'STAFF',
    };
    this.showform = true;
  }

  closeform() {
    this.showform = false;
    this.isEditMode = false;
  }

  register() {
    this.userService.adminCreateUser(this.newUser).subscribe({
      next: (res: any) => {
        this.loadUsers(); // Refresh the table
        this.closeform();
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('Error adding user:', err.error || err.message);
        alert('Failed to add user. Check console for details.');
        this.cdr.detectChanges();
      },
    });
  }

  editUser(user: any) {
    this.isEditMode = true;
    this.newUser = { ...user };
    this.showform = true;
  }

  updateUser() {
    if (this.newUser.id) {
      this.userService.updateUser(this.newUser.id, this.newUser).subscribe({
        next: (res: any) => {
          this.loadUsers();
          this.closeform();
          this.cdr.detectChanges();
        },
        error: (err: any) => {
          console.error('Error updating user:', err);
          alert('Failed to update user.');
          this.cdr.detectChanges();
        }
      });
    }
  }

  deleteUser(id: number) {
    if (confirm('Are you sure you want to delete this user?')) {
      this.userService.deleteUser(id).subscribe({
        next: () => {
          this.loadUsers();
          this.cdr.detectChanges();
        },
        error: (err: any) => {
          console.error('Error deleting user:', err);
          this.cdr.detectChanges();
        },
      });
    }
  }

  
}
