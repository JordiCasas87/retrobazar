import { CommonModule, Location } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { switchMap } from 'rxjs';
import { CatalogService } from './catalog.service';
import { Product } from './product.model';

@Component({
  selector: 'rb-product-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.css'
})
export class ProductDetailComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly catalog = inject(CatalogService);
  private readonly location = inject(Location);

  readonly product = signal<Product | null>(null);
  readonly loading = signal(true);
  readonly error = signal(false);
  readonly activeImage = signal(0);
  readonly fallbackImage = 'assets/product-placeholder-retro-bazar-v6.png';

  constructor() {
    this.route.paramMap.pipe(
      switchMap((params) => this.catalog.byId(params.get('id') ?? ''))
    ).subscribe({
      next: (product) => {
        this.product.set(product);
        this.loading.set(false);
      },
      error: () => {
        this.error.set(true);
        this.loading.set(false);
      }
    });
  }

  goBack(): void {
    this.location.back();
  }

  selectImage(index: number): void {
    this.activeImage.set(index);
  }

  useFallback(event: Event): void {
    const image = event.target as HTMLImageElement;
    if (image.dataset['fallbackApplied']) return;
    image.dataset['fallbackApplied'] = 'true';
    image.src = this.fallbackImage;
  }

  categoryLabel(category: string): string {
    return {
      GADGETS: 'Gadgets', GAMING: 'Retro gaming',
      SETUP_ACCESSORIES: 'Setup y accesorios', OTHERS: 'Otros hallazgos'
    }[category] ?? category;
  }
}
