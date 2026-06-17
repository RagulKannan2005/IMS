import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Auth } from '../../app/services/auth';
import { Router, RouterLink } from "@angular/router";
@Component({
  selector: 'app-register',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {

  private fb= inject(FormBuilder);
  private authService= inject(Auth);
  constructor(private router:Router){}


  registerForm =this.fb.group({
    username : [''],
    firstName : [''],
    lastName : [''],
    email : [''],
    password : [''],
    // confirmPassword : [''],
    phone_number : [''],
    role : ['']
  })

  onSubmit(){
    this.authService.register(this.registerForm.value)
      .subscribe({
        next:(response)=>{
          console.log('SuccessFully Registered',response);
          alert('Registeer Successfull');
          // this.router.navigate
          this.router.navigate(['/home']);

        },
        error:(error)=>{
          console.log(error);
          alert('Failed to register');
        }
      });
  }
  
}
