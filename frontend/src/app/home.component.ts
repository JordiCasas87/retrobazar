import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Observable } from 'rxjs';
import { CatalogService } from './catalog.service';
import { Product } from './product.model';

@Component({
  selector: 'rb-home',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './app.component.css'
})
export class HomeComponent {
  private readonly catalog = inject(CatalogService);
  private pointerFrame: number | null = null;
  private lastPointer = { x: 0, y: 0 };

  readonly products = signal<Product[]>([]);
  readonly catalogLoading = signal(true);
  readonly catalogError = signal(false);
  readonly productFallbackImage = 'assets/product-placeholder-retro-bazar-v6.png';
  readonly heroLines = [Array.from('El futuro'), Array.from('también fue retro.')];
  readonly mobileHeroLines = [
    Array.from('El futuro'),
    Array.from('también fue'),
    Array.from('retro.')
  ];
  readonly visibleProducts = computed(() => this.products().slice(0, 5));

  constructor() {
    this.loadProducts(this.catalog.byCategory('GAMING'));
  }

  retryCatalog(): void {
    this.loadProducts(this.catalog.byCategory('GAMING'));
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

  private loadProducts(request: Observable<Product[]>): void {
    this.catalogLoading.set(true);
    this.catalogError.set(false);
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

}
