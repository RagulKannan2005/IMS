import { inject } from "@angular/core";
import { Router, CanActivateFn } from "@angular/router";
import { Auth } from "../services/auth";

export const supplierGuard: CanActivateFn = (route, state) => {
    const authService = inject(Auth);
    const router = inject(Router);
    const user = authService.getuser();
    if (user && user.role === 'SUPPLIER') {
        return true;
    }
    router.navigate(['/login']);
    return false;
}