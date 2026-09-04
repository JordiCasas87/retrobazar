import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'rb-root',
  standalone: true,
  imports: [RouterLink, RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  private readonly router = inject(Router);
  readonly menuOpen = signal(false);
  readonly searchOpen = signal(false);
  readonly year = new Date().getFullYear();

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
}
