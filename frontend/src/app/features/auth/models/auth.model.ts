export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  accessToken: string;
  tokenType: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
  newProductEmailNotificationsEnabled: boolean;
}

export interface RegisterResponse {
  id: string;
  name: string;
  email: string;
  role: UserRole;
  newProductEmailNotificationsEnabled: boolean;
}

export type UserRole = 'ADMIN' | 'CUSTOMER';

export interface JwtPayload {
  sub: string;
  role: UserRole;
  exp: number;
}

export interface UserProfile {
  name: string;
  email: string;
  newProductEmailNotificationsEnabled: boolean;
}
