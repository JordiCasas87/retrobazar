import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { catchError, map, Observable, of, switchMap, tap } from 'rxjs';
import { API_BASE_URL } from '../../../core/config/api.config';
import { JwtPayload, LoginRequest, LoginResponse, RegisterRequest, RegisterResponse, UserProfile, UserRole } from '../models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly tokenKey = 'retro-bazar-access-token';
  private readonly currentUserState = signal<UserProfile | null>(null);
  private readonly currentRoleState = signal<UserRole | null>(this.readPayload()?.role ?? null);

  readonly currentUser = this.currentUserState.asReadonly();
  readonly currentRole = this.currentRoleState.asReadonly();

  login(request: LoginRequest): Observable<void> {
    return this.http.post<LoginResponse>(`${API_BASE_URL}/auth/login`, request).pipe(
      tap((response) => {
        sessionStorage.setItem(this.tokenKey, response.accessToken);
        this.currentRoleState.set(this.readPayload()?.role ?? null);
      }),
      switchMap(() => this.loadCurrentUser()),
      map(() => undefined)
    );
  }

  register(request: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(`${API_BASE_URL}/auth/register`, request);
  }

  loadCurrentUser(): Observable<UserProfile | null> {
    if (!this.accessToken()) return of(null);

    return this.http.get<UserProfile>(`${API_BASE_URL}/users/me`).pipe(
      tap((profile) => this.currentUserState.set(profile)),
      catchError(() => {
        this.logout();
        return of(null);
      })
    );
  }

  accessToken(): string | null {
    return sessionStorage.getItem(this.tokenKey);
  }

  role(): UserRole | null {
    return this.currentRoleState();
  }

  logout(): void {
    sessionStorage.removeItem(this.tokenKey);
    this.currentUserState.set(null);
    this.currentRoleState.set(null);
  }

  private readPayload(): JwtPayload | null {
    const token = this.accessToken();
    if (!token) return null;

    try {
      const encodedPayload = token.split('.')[1];
      if (!encodedPayload) return null;
      const base64 = encodedPayload.replace(/-/g, '+').replace(/_/g, '/');
      const paddedBase64 = base64.padEnd(Math.ceil(base64.length / 4) * 4, '=');
      const payload = JSON.parse(atob(paddedBase64)) as JwtPayload;

      if (!payload.exp || payload.exp * 1000 <= Date.now()) {
        sessionStorage.removeItem(this.tokenKey);
        return null;
      }

      return payload;
    } catch {
      sessionStorage.removeItem(this.tokenKey);
      return null;
    }
  }
}
