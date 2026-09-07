import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
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
