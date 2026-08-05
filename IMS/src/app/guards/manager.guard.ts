import { inject } from '@angular/core';
import { Router, type CanActivateFn } from '@angular/router';
import { Auth } from '../services/auth';

export const managerGuard: CanActivateFn = (route, state) => {
  const authService = inject(Auth);
  const router = inject(Router);
  
  const user = authService.getuser();
  
  if (user && (user.role === 'MANAGER' || user.role === 'ADMIN')) {
    return true; // Allow access to MANAGER or ADMIN
  }
  
  // If not logged in or wrong role, redirect to login
  router.navigate(['/login']);
  return false;
};
