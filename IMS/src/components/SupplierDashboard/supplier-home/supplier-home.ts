import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { Auth } from '../../../app/services/auth';

@Component({
  selector: 'app-supplier-home',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterOutlet],
  templateUrl: './supplier-home.html',
  styleUrl: './supplier-home.css',
})
export class SupplierHome implements OnInit {
  user: any;
  authService = inject(Auth);
  router = inject(Router);

  ngOnInit() {
    this.user = this.authService.getuser();
  }
}
