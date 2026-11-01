import { HttpErrorResponse } from '@angular/common/http';
import { Component, HostListener, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { AuthService } from '../../data-access/auth.service';

@Component({
  selector: 'rb-account',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent {
  readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  readonly activeModal = signal<'login' | 'register' | null>(null);
  readonly loginLoading = signal(false);
  readonly loginError = signal<string | null>(null);
  readonly loginSuccess = signal<string | null>(null);
  readonly registerLoading = signal(false);
  readonly registerError = signal<string | null>(null);
  readonly loginForm = new FormGroup({
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.email]
    }),
    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    })
  });
  readonly registerForm = new FormGroup({
    name: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(255)]
    }),
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.email, Validators.maxLength(255)]
    }),
    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(8), Validators.maxLength(72)]
    }),
    confirmPassword: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    newProductEmailNotificationsEnabled: new FormControl(false, { nonNullable: true })
  });

  openModal(modal: 'login' | 'register'): void {
    this.loginError.set(null);
    this.registerError.set(null);
    this.activeModal.set(modal);
  }

  closeModal(): void {
    this.activeModal.set(null);
  }

  closeFromBackdrop(event: MouseEvent): void {
    if (event.target === event.currentTarget) this.closeModal();
  }

  @HostListener('document:keydown.escape')
  closeWithEscape(): void {
    this.closeModal();
  }

  submitLogin(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      this.loginError.set('Revisa el email y la contraseña.');
      return;
    }

    this.loginLoading.set(true);
    this.loginError.set(null);

    this.auth.login(this.loginForm.getRawValue()).pipe(
      finalize(() => this.loginLoading.set(false))
    ).subscribe({
      next: () => {
        const role = this.auth.role();
        this.loginForm.reset();
        this.closeModal();

        if (role === 'ADMIN') {
          void this.router.navigate(['/admin/productos']);
          return;
        }

        this.loginSuccess.set('Has iniciado sesión correctamente.');
      },
      error: (error: HttpErrorResponse) => {
        this.loginError.set(
          error.status === 401
            ? 'El email o la contraseña no son correctos.'
            : 'No hemos podido iniciar sesión. Inténtalo de nuevo.'
        );
      }
    });
  }

  submitRegistration(): void {
    const value = this.registerForm.getRawValue();

    if (this.registerForm.invalid || value.password !== value.confirmPassword) {
      this.registerForm.markAllAsTouched();
      this.registerError.set(
        value.password !== value.confirmPassword
          ? 'Las contraseñas no coinciden.'
          : 'Revisa los campos obligatorios.'
      );
      return;
    }

    this.registerLoading.set(true);
    this.registerError.set(null);

    this.auth.register({
      name: value.name.trim(),
      email: value.email.trim(),
      password: value.password,
      newProductEmailNotificationsEnabled: value.newProductEmailNotificationsEnabled
    }).pipe(
      finalize(() => this.registerLoading.set(false))
    ).subscribe({
      next: () => {
        this.loginForm.controls.email.setValue(value.email.trim());
        this.registerForm.reset();
        this.loginSuccess.set('Cuenta creada correctamente. Ya puedes iniciar sesión.');
        this.activeModal.set('login');
      },
      error: (error: HttpErrorResponse) => {
        this.registerError.set(
          error.status === 409
            ? 'Ya existe una cuenta con este email.'
            : 'No hemos podido crear la cuenta. Inténtalo de nuevo.'
        );
      }
    });
  }

  logout(): void {
    this.auth.logout();
    this.loginSuccess.set(null);
  }
}
