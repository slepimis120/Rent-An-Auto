import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const merchantGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);

  const role = auth.getRole();

  if (role! === 'ROLE_MERCHANT') {
    return true;
  }

  return router.parseUrl('/login');
};
