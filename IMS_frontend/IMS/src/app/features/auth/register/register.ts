import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ThemeService } from '../../../core/services/theme.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    RouterLink
  ],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
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
    phone_number: new FormControl('', [
      Validators.required,
      Validators.pattern(/^\+?[0-9\s-]{10,15}$/)
    ]),
    role: new FormControl('STAFF', [
      Validators.required
    ]),
    supplierId: new FormControl<number | null>(null)
  });

  roles = ['ADMIN', 'MANAGER', 'STAFF', 'SUPPLIER'];

  constructor(
    private router: Router,
    public themeService: ThemeService
  ) {
    // Dynamically adjust validation for supplierId when role is changed
    this.registerForm.get('role')?.valueChanges.subscribe(role => {
      const supplierIdCtrl = this.registerForm.get('supplierId');
      if (role === 'SUPPLIER') {
        supplierIdCtrl?.setValidators([Validators.required, Validators.min(1)]);
      } else {
        supplierIdCtrl?.clearValidators();
        supplierIdCtrl?.setValue(null);
      }
      supplierIdCtrl?.updateValueAndValidity();
    });
  }

  submit() {
    console.log('Register Form Value:', this.registerForm.value);
    console.log('Register Form Valid:', this.registerForm.valid);

    if (this.registerForm.valid) {
      console.log('Registration Request successfully generated!');
      // Here you will integrate the backend API post call to /api/v1/auth/register or similar endpoint.
      // E.g., this.authService.register(this.registerForm.value).subscribe(...)
      
      // Navigate to login after successful register
      this.router.navigate(['/login']);
    } else {
      console.log('Registration Form is Invalid');
      this.registerForm.markAllAsTouched();
    }
  }
}
