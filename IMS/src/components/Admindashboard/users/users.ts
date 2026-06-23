import { Component, OnInit, inject } from '@angular/core';
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

  users: any[] = [];
  filteredUsers: any[] = [];
  showform = false;

  newUser = {
    username: '',
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    phone_number: '',
    role: 'STAFF'
  };

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.userService.getAllUsers().subscribe({
      next: (data: any[]) => {
        this.users = data;
        this.filteredUsers = data;
      },
      error: (err: any) => console.error('Error fetching users:', err),
    });
  }

  searchUser(event: any) {
    const term = event.target.value.toLowerCase();
    this.filteredUsers = this.users.filter(
      (u: any) =>
        u.firstName?.toLowerCase().includes(term) ||
        u.lastName?.toLowerCase().includes(term) ||
        u.email?.toLowerCase().includes(term) ||
        u.role?.toLowerCase().includes(term)
    );
  }

  openform() {
    this.showform = true;
  }

  closeform() {
    this.showform = false;
  }

  register() {
    this.userService.adminCreateUser(this.newUser).subscribe({
      next: (res: any) => {
        this.loadUsers(); // Refresh the table
        this.closeform();
        this.newUser = { username: '', firstName: '', lastName: '', email: '', password: '', phone_number: '', role: 'STAFF' };
      },
      error: (err: any) => {
        console.error('Error adding user:', err.error || err.message);
        alert('Failed to add user. Check console for details.');
      }
    });
  }

  deleteUser(id: number) {
    if (confirm('Are you sure you want to delete this user?')) {
      this.userService.deleteUser(id).subscribe({
        next: () => this.loadUsers(),
        error: (err: any) => console.error('Error deleting user:', err),
      });
    }
  }
}
