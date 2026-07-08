import { inject } from '@angular/core';
import { Router, type CanActivateFn } from '@angular/router';
import { Auth } from '../services/auth';

export const adminGuard: CanActivateFn = (route, state) => {
  const authService = inject(Auth);
  const router = inject(Router);
  
  const user = authService.getuser();
  
  if (user && (user.role === 'ADMIN' || user.role === 'MANAGER')) {
    return true; // Allow access to ADMIN or MANAGER
  }
  
  // If not logged in or wrong role, redirect to login
  router.navigate(['/login']);
  return false;
};
