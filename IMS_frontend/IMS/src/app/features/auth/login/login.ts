import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ThemeService } from '../../../core/services/theme.service';
import { AuthService } from '../../../core/services/auth.service';
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
    public themeService: ThemeService,
    private authService:AuthService
  ) {}

  submit() {
    if(this.loginform.valid){

      this.authService.loginuser(this.loginform.value)
      .subscribe({
        next: (res: any) => {
          console.log('Login Successful');
          this.authService.login({
            username: res.username,
            email: res.email,
            role: res.role,
            firstName: res.firstName,
            lastName: res.lastName,
            id: res.id,
            token: res.token
          });
          this.router.navigate(['/dashboard']);
        },
        error: (err) => {
          console.log('Login Failed');
          console.log(err);
        }
      });
    }else {
      console.log('Login Failed');

      console.log('email Errors:',
        this.loginform.get('email')?.errors
      );
      console.log('Password Errors:',
        this.loginform.get('password')?.errors
      );

      this.loginform.markAllAsTouched();
    }
  }
}