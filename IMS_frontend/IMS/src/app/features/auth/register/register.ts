import { Component, signal } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ThemeService } from '../../../core/services/theme.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    RouterLink,
  ],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  errorMessage = signal<string | null>(null);
  successMessage = signal<string | null>(null);
  isSubmitting = signal(false);

  registerForm = new FormGroup({
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(12)
    ]),
    firstName: new FormControl('', [
      Validators.required
    ]),
    lastName: new FormControl('', [
      Validators.required
    ]),
    email: new FormControl('', [
      Validators.required,
      Validators.email
    ]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(4),
      Validators.maxLength(12),
      Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).+$/)
    ]),
    phoneNumber: new FormControl('', [
      Validators.required,
      Validators.pattern(/^\+?[0-9\s-]{10,15}$/)
    ]),
    role: new FormControl('STAFF', [
      Validators.required
    ]),
    companyName: new FormControl(''),
    address: new FormControl('')
  });

  roles = ['ADMIN', 'MANAGER', 'STAFF', 'SUPPLIER'];

  constructor(
    private router: Router,
    public themeService: ThemeService,
    private authService: AuthService
  ) {}

  submit() {
    this.errorMessage.set(null);
    this.successMessage.set(null);

    if (this.registerForm.valid) {
      this.isSubmitting.set(true);
      const payload: any = { ...this.registerForm.value };

      // Clean empty fields
      if (!payload.companyName) delete payload.companyName;
      if (!payload.address) delete payload.address;

      this.authService.register(payload)
        .subscribe({
          next: () => {
            this.isSubmitting.set(false);
            this.successMessage.set('Account registered successfully! Redirecting to login...');
            setTimeout(() => {
              this.router.navigate(['/login']);
            }, 1200);
          },
          error: (error) => {
            this.isSubmitting.set(false);
            console.error('Register Error:', error);
            if (error.status === 0) {
              this.errorMessage.set('Cannot connect to backend server. Please verify Spring Boot is running on port 8082.');
            } else {
              const msg = error.error?.message || error.error?.error || 'Registration failed. Please check your details.';
              this.errorMessage.set(msg);
            }
          }
        });
    } else {
      this.registerForm.markAllAsTouched();
    }
  }
}
