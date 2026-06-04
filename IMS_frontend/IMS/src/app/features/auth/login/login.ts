import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ThemeService } from '../../../core/services/theme.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    RouterLink
  ],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

  loginform = new FormGroup({
    email: new FormControl('', [
      Validators.required,
      Validators.email
    ]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(4),
      Validators.maxLength(13),
      Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).+$/)
    ])
  });

  constructor(
    private router: Router,
    public themeService: ThemeService
  ) {}

  submit() {
    console.log('Form Value:', this.loginform.value);
    console.log('Form Valid:', this.loginform.valid);

    if (this.loginform.valid) {
      console.log('Login Successful');

      const email = this.loginform.value.email;
      const password = this.loginform.value.password;

      console.log(email);
      console.log(password);
      this.router.navigate(['/dashboard']);

    } else {
      console.log('Login Failed');

      console.log('email Errors:',
        this.loginform.get('email')?.errors);

      console.log('Password Errors:',
        this.loginform.get('password')?.errors);

      this.loginform.markAllAsTouched();
    }
  }
}