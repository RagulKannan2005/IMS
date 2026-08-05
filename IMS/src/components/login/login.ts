import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Auth } from '../../app/services/auth';
import { email } from '@angular/forms/signals';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private fb = inject(FormBuilder);
  private authService = inject(Auth);

  loginForm = this.fb.group({
    email: [''],
    password: [''],
  });
  constructor(private router: Router) {}

  onSubmit() {
    this.authService.login(this.loginForm.value).subscribe({
      next: (response: any) => {
        localStorage.setItem('token', response.token);
        localStorage.setItem(
          'user',
          JSON.stringify({
            id: response.id,
            username: response.username,
            email: response.email,
            role: response.role,
            supplierId: response.supplierId,
          }),
        );
        if (response.role == 'SUPPLIER') {
          this.router.navigate(['/supplier']);
        }
        if (response.role == 'ADMIN') {
          this.router.navigate(['/admin']);
        }
        if (response.role == 'MANAGER') {
          this.router.navigate(['/manager']);
        }
        console.log('login successfull', response);
        alert('login successfull');
      },
      error: (err) => {
        console.log('login failed', err);
        alert('login failed');
        this.loginForm.reset();
      },
    });
  }
}
