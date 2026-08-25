import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterOutlet } from '@angular/router';
import { filter, Observable } from 'rxjs';
import { CatalogService } from './catalog.service';
import { Product, ProductCategory } from './product.model';

@Component({
  selector: 'rb-root',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  private readonly catalog = inject(CatalogService);
  private readonly router = inject(Router);
  private pointerFrame: number | null = null;
  private lastPointer = { x: 0, y: 0 };
  readonly menuOpen = signal(false);
  readonly searchOpen = signal(false);
  readonly selectedCategory = signal<ProductCategory | 'ALL'>('ALL');
  readonly products = signal<Product[]>([]);
  readonly catalogLoading = signal(true);
  readonly catalogError = signal(false);
  readonly catalogTitle = signal('Objetos más buscados');
  readonly isProductPage = signal(window.location.pathname.startsWith('/producto/'));
  readonly year = new Date().getFullYear();
  readonly productFallbackImage = 'assets/product-placeholder-retro-bazar-v6.png';
  readonly heroLines = [
    Array.from('El futuro'),
    Array.from('también fue retro.')
  ];

  readonly visibleProducts = computed(() => {
    return this.products().slice(0, 5);
  });

  constructor() {
    this.router.events.pipe(
      filter((event): event is NavigationEnd => event instanceof NavigationEnd)
    ).subscribe((event) => this.isProductPage.set(event.urlAfterRedirects.startsWith('/producto/')));
    this.loadAll();
  }

  selectCategory(category: ProductCategory | 'ALL'): void {
    this.selectedCategory.set(category);
    const request = category === 'ALL' ? this.catalog.list() : this.catalog.byCategory(category);
    const title = category === 'ALL' ? 'Objetos más buscados' : this.categoryLabel(category);
    this.loadProducts(request, title);
    document.querySelector('#seleccion')?.scrollIntoView({ behavior: 'smooth' });
  }

  searchProducts(text: string): void {
    const query = text.trim();
    if (!query) {
      this.loadAll();
      return;
    }

    this.selectedCategory.set('ALL');
    this.searchOpen.set(false);
    this.loadProducts(this.catalog.search(query), `Resultados para “${query}”`);
    queueMicrotask(() => document.querySelector('#seleccion')?.scrollIntoView({ behavior: 'smooth' }));
  }

  retryCatalog(): void {
    this.loadAll();
  }

  private loadAll(): void {
    this.selectedCategory.set('ALL');
    this.loadProducts(this.catalog.list(), 'Objetos más buscados');
  }

  private loadProducts(request: Observable<Product[]>, title: string): void {
    this.catalogLoading.set(true);
    this.catalogError.set(false);
    this.catalogTitle.set(title);
    request.subscribe({
      next: (products) => {
        this.products.set(products);
        this.catalogLoading.set(false);
      },
      error: () => {
        this.products.set([]);
        this.catalogError.set(true);
        this.catalogLoading.set(false);
      }
    });
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
    if (this.isProductPage()) {
      void this.router.navigate(['/']).then(() => this.searchOpen.set(true));
      return;
    }
    this.searchOpen.update((open) => !open);
  }

  scatterHeroLetters(event: MouseEvent): void {
    if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) return;

    const heading = event.currentTarget as HTMLElement;
    const pointer = { x: event.clientX, y: event.clientY };
    const velocity = {
      x: Math.max(-28, Math.min(28, pointer.x - this.lastPointer.x)),
      y: Math.max(-28, Math.min(28, pointer.y - this.lastPointer.y))
    };
    this.lastPointer = pointer;

    if (this.pointerFrame !== null) cancelAnimationFrame(this.pointerFrame);
    this.pointerFrame = requestAnimationFrame(() => {
      heading.querySelectorAll<HTMLElement>('.hero-letter').forEach((letter, index) => {
        const rect = letter.getBoundingClientRect();
        const deltaX = rect.left + rect.width / 2 - pointer.x;
        const deltaY = rect.top + rect.height / 2 - pointer.y;
        const distance = Math.hypot(deltaX, deltaY);
        const force = Math.max(0, 1 - distance / 180);
        const direction = index % 2 === 0 ? 1 : -1;
        const translateX = (deltaX * .14 + velocity.x * 1.2 * direction) * force;
        const translateY = (deltaY * .12 + velocity.y * .9) * force;
        const rotation = (velocity.x * .65 + direction * 10) * force;

        letter.style.setProperty('--scatter-x', `${translateX}px`);
        letter.style.setProperty('--scatter-y', `${translateY}px`);
        letter.style.setProperty('--scatter-rotate', `${rotation}deg`);
        letter.style.setProperty('--scatter-scale', `${1 + force * .08}`);
      });
      this.pointerFrame = null;
    });
  }

  restoreHeroLetters(event: MouseEvent): void {
    const heading = event.currentTarget as HTMLElement;
    heading.querySelectorAll<HTMLElement>('.hero-letter').forEach((letter) => {
      letter.style.removeProperty('--scatter-x');
      letter.style.removeProperty('--scatter-y');
      letter.style.removeProperty('--scatter-rotate');
      letter.style.removeProperty('--scatter-scale');
    });
    this.lastPointer = { x: 0, y: 0 };
  }

  useProductFallback(event: Event): void {
    const image = event.target as HTMLImageElement;
    if (image.dataset['fallbackApplied']) return;
    image.dataset['fallbackApplied'] = 'true';
    image.src = this.productFallbackImage;
  }

  categoryLabel(category: ProductCategory): string {
    return {
      GADGETS: 'Gadgets', GAMING: 'Retro gaming',
      SETUP_ACCESSORIES: 'Setup', OTHERS: 'Hallazgos'
    }[category];
  }

}
