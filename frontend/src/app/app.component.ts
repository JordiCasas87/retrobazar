import { Component, inject, signal } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterOutlet } from '@angular/router';
import { filter } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { AuthService } from './features/auth/data-access/auth.service';

@Component({
  selector: 'rb-root',
  standalone: true,
  imports: [RouterLink, RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  private readonly router = inject(Router);
  readonly auth = inject(AuthService);
  readonly menuOpen = signal(false);
  readonly searchOpen = signal(false);
  readonly isHome = signal(this.isHomeUrl(this.router.url));
  readonly year = new Date().getFullYear();

  constructor() {
    this.auth.loadCurrentUser().pipe(takeUntilDestroyed()).subscribe();

    this.router.events
      .pipe(
        filter((event): event is NavigationEnd => event instanceof NavigationEnd),
        takeUntilDestroyed()
      )
      .subscribe((event) => this.isHome.set(this.isHomeUrl(event.urlAfterRedirects)));
  }

  toggleMenu(): void {
    const nextState = !this.menuOpen();
    this.menuOpen.set(nextState);
    if (nextState) this.searchOpen.set(false);
  }

  openSearchFromMenu(): void {
    this.menuOpen.set(false);
    this.toggleSearch();
  }

  toggleSearch(): void {
    this.searchOpen.update((open) => !open);
  }

  showIdea(event: Event): void {
    event.preventDefault();
    this.menuOpen.set(false);
    void this.router.navigate(['/'], { fragment: 'manifiesto' }).then(() => {
      requestAnimationFrame(() => {
        document.getElementById('manifiesto')?.scrollIntoView({
          behavior: 'smooth',
          block: 'center'
        });
      });
    });
  }

  searchProducts(text: string): void {
    const search = text.trim();
    this.searchOpen.set(false);
    void this.router.navigate(['/catalogo'], {
      queryParams: search ? { search } : {}
    });
  }

  logout(): void {
    this.auth.logout();
    this.menuOpen.set(false);
    this.searchOpen.set(false);
    void this.router.navigate(['/']);
  }

  private isHomeUrl(url: string): boolean {
    return url.split(/[?#]/)[0] === '/';
  }
}
