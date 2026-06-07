import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const allowedRoles = route.data?.['roles'] as string[];
  const userRole = authService.userRole();

  if (!allowedRoles || (userRole && allowedRoles.includes(userRole))) {
    return true;
  }

  // User does not have access, redirect to access denied page
  console.warn(`User with role ${userRole} attempted to access unauthorized path: ${state.url}`);
  router.navigate(['/403']);
  return false;
};
