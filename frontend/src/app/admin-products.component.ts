import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AdminProductService } from './admin-product.service';
import { Product, ProductCategory } from './product.model';

@Component({
  selector: 'rb-admin-products',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './admin-products.component.html',
  styleUrls: ['./catalog.component.css', './admin-products.component.css']
})
export class AdminProductsComponent {
  private readonly adminProducts = inject(AdminProductService);
  private readonly pageSize = 10;

  readonly products = signal<Product[]>([]);
  readonly selectedCategory = signal<ProductCategory | 'ALL'>('ALL');
  readonly currentPage = signal(1);
  readonly loading = signal(true);
  readonly error = signal(false);
  readonly deletingId = signal<string | null>(null);
  readonly feedback = signal('');
  readonly fallbackImage = 'assets/product-placeholder-retro-bazar-v6.png';

  readonly filteredProducts = computed(() => {
    const category = this.selectedCategory();
    return category === 'ALL'
      ? this.products()
      : this.products().filter((product) => product.category === category);
  });
  readonly totalPages = computed(() => Math.max(1, Math.ceil(this.filteredProducts().length / this.pageSize)));
  readonly visibleProducts = computed(() => {
    const start = (this.currentPage() - 1) * this.pageSize;
    return this.filteredProducts().slice(start, start + this.pageSize);
  });
  readonly pages = computed(() => Array.from({ length: this.totalPages() }, (_, index) => index + 1));

  constructor() {
    this.loadProducts();
  }

  selectCategory(category: ProductCategory | 'ALL'): void {
    this.selectedCategory.set(category);
    this.currentPage.set(1);
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages() || page === this.currentPage()) return;
    this.currentPage.set(page);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  loadProducts(): void {
    this.loading.set(true);
    this.error.set(false);
    this.adminProducts.list().subscribe({
      next: (products) => {
        this.products.set(products);
        this.currentPage.set(Math.min(this.currentPage(), Math.max(1, Math.ceil(this.filteredProducts().length / this.pageSize))));
        this.loading.set(false);
      },
      error: () => {
        this.error.set(true);
        this.loading.set(false);
      }
    });
  }

  deleteProduct(product: Product): void {
    const confirmed = window.confirm(`¿Eliminar definitivamente “${product.name}”?`);
    if (!confirmed) return;

    this.deletingId.set(product.id);
    this.feedback.set('');
    this.adminProducts.delete(product.id).subscribe({
      next: () => {
        this.products.update((products) => products.filter((item) => item.id !== product.id));
        this.currentPage.set(Math.min(this.currentPage(), this.totalPages()));
        this.deletingId.set(null);
        this.feedback.set(`“${product.name}” se ha eliminado.`);
      },
      error: () => {
        this.deletingId.set(null);
        this.feedback.set('No se ha podido eliminar el producto. Inténtalo de nuevo.');
      }
    });
  }

  useFallback(event: Event): void {
    const image = event.target as HTMLImageElement;
    if (image.dataset['fallbackApplied']) return;
    image.dataset['fallbackApplied'] = 'true';
    image.src = this.fallbackImage;
  }
}
