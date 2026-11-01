import { CommonModule } from '@angular/common';
import { Component, computed, DestroyRef, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Observable } from 'rxjs';
import { CatalogService } from '../../../catalog/data-access/catalog.service';
import { Product } from '../../../catalog/models/product.model';
import { HeroScatterDirective } from '../../ui/hero-scatter.directive';

@Component({
  selector: 'rb-home',
  standalone: true,
  imports: [CommonModule, RouterLink, HeroScatterDirective],
  templateUrl: './home.component.html',
  styleUrl: '../../../../app.component.css'
})
export class HomeComponent {
  private readonly catalog = inject(CatalogService);
  private readonly destroyRef = inject(DestroyRef);
  private rotationUpdateTimer?: number;
  private rotationEndTimer?: number;
  readonly products = signal<Product[]>([]);
  readonly catalogLoading = signal(true);
  readonly catalogError = signal(false);
  readonly productOffset = signal(0);
  readonly productsFlipping = signal(false);
  readonly activeHero = signal(0);
  readonly carouselEnabled = computed(() => this.products().slice(0, 10).length > 5);
  readonly productFallbackImage = 'assets/product-placeholder-retro-bazar-v6.jpg';
  readonly heroSlides = [
    {
      src: 'assets/hero-retro-products-v2.jpg',
      alt: 'Selección de productos retro: consola portátil, teclado, cámaras, robot y pantalla'
    },
    {
      src: 'assets/zelda-hero.jpg',
      alt: 'Composición retro inspirada en The Legend of Zelda'
    }
  ];
  readonly heroLines = [Array.from('El futuro'), Array.from('también fue retro.')];
  readonly mobileHeroLines = [
    Array.from('El futuro'),
    Array.from('también fue'),
    Array.from('retro.')
  ];
  readonly visibleProducts = computed(() => {
    const pool = this.products().slice(0, 10);
    if (pool.length <= 5) return pool;

    const incomingIndex = (this.productOffset() - 1 + pool.length) % pool.length;
    return [
      pool[incomingIndex],
      ...Array.from({ length: 5 }, (_, index) => pool[(this.productOffset() + index) % pool.length])
    ];
  });

  constructor() {
    this.loadProducts(this.catalog.byCategory('GAMING'));

    const rotationInterval = window.setInterval(() => this.rotateProducts(), 8000);
    const heroInterval = window.setInterval(
      () => this.activeHero.update((index) => (index + 1) % this.heroSlides.length),
      15000
    );
    this.destroyRef.onDestroy(() => {
      window.clearInterval(rotationInterval);
      window.clearInterval(heroInterval);
      if (this.rotationUpdateTimer) window.clearTimeout(this.rotationUpdateTimer);
      if (this.rotationEndTimer) window.clearTimeout(this.rotationEndTimer);
    });
  }

  retryCatalog(): void {
    this.loadProducts(this.catalog.byCategory('GAMING'));
  }

  useProductFallback(event: Event): void {
    const image = event.target as HTMLImageElement;
    if (image.dataset['fallbackApplied']) return;
    image.dataset['fallbackApplied'] = 'true';
    image.src = this.productFallbackImage;
  }

  private rotateProducts(): void {
    const poolSize = Math.min(this.products().length, 10);
    if (poolSize <= 5 || this.productsFlipping()) return;

    this.productsFlipping.set(true);
    this.rotationUpdateTimer = window.setTimeout(() => {
      this.productOffset.update((offset) => (offset - 1 + poolSize) % poolSize);
      this.productsFlipping.set(false);
    }, 820);
  }

  private loadProducts(request: Observable<Product[]>): void {
    this.catalogLoading.set(true);
    this.catalogError.set(false);
    request.subscribe({
      next: (products) => {
        this.products.set(products);
        this.productOffset.set(0);
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
