import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { API_BASE_URL } from '../config/api.config';
import { AuthService } from '../../features/auth/data-access/auth.service';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const token = inject(AuthService).accessToken();
  const isApiRequest = request.url.startsWith(API_BASE_URL);
  const isAuthenticationRequest = request.url.includes('/auth/login') || request.url.includes('/auth/register');

  if (!token || !isApiRequest || isAuthenticationRequest) return next(request);

  return next(request.clone({
    setHeaders: { Authorization: `Bearer ${token}` }
  }));
};
