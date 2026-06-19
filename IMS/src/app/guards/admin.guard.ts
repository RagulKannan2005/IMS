import { inject } from '@angular/core';
import { Router, type CanActivateFn } from '@angular/router';
import { Auth } from '../services/auth';

export const adminGuard: CanActivateFn = (route, state) => {
  const authService = inject(Auth);
  const router = inject(Router);
  
  const user = authService.getuser();
  
  if (user && user.role === 'ADMIN') {
    return true; // Allow access
  }
  
  // If not logged in or not admin, redirect to login
  router.navigate(['/login']);
  return false;
};
