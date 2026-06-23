import { inject } from '@angular/core';
import { Router, type CanActivateFn } from '@angular/router';
import { Auth } from '../services/auth';

export const adminGuard: CanActivateFn = (route, state) => {
  const authService = inject(Auth);
  const router = inject(Router);
  
  const user = authService.getuser();
  
  if (user) {
    return true; // Allow access to all logged-in users
  }
  
  // If not logged in, redirect to login
  router.navigate(['/login']);
  return false;
};
