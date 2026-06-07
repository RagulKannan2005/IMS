import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-access-denied',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './access-denied.html',
  styleUrl: './access-denied.css'
})
export class AccessDenied {
  constructor(
    private router: Router,
    public authService: AuthService
  ) {}

  goHome(): void {
    this.router.navigate(['/dashboard']);
  }
}
