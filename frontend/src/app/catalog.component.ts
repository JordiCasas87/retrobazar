import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Observable } from 'rxjs';
import { CatalogService } from './catalog.service';
import { Product, ProductCategory } from './product.model';

@Component({
  selector: 'rb-catalog',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './catalog.component.html',
  styleUrl: './catalog.component.css'
})
export class CatalogComponent {
  private readonly catalog = inject(CatalogService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly pageSize = 10;

  readonly products = signal<Product[]>([]);
  readonly loading = signal(true);
  readonly error = signal(false);
  readonly selectedCategory = signal<ProductCategory | 'ALL'>('ALL');
  readonly searchText = signal('');
  readonly currentPage = signal(1);
  readonly fallbackImage = 'assets/product-placeholder-retro-bazar-v6.png';

  readonly title = computed(() => {
    if (this.searchText()) return `Resultados para “${this.searchText()}”`;
    const category = this.selectedCategory();
    if (category !== 'ALL') return this.categoryLabel(category);
    return 'Todos los productos';
  });

  readonly totalPages = computed(() => Math.max(1, Math.ceil(this.products().length / this.pageSize)));
  readonly visibleProducts = computed(() => {
    const start = (this.currentPage() - 1) * this.pageSize;
    return this.products().slice(start, start + this.pageSize);
  });
  readonly pages = computed(() => Array.from({ length: this.totalPages() }, (_, index) => index + 1));

  constructor() {
    this.route.queryParamMap.subscribe((params) => {
      const search = params.get('search')?.trim() ?? '';
      const category = this.parseCategory(params.get('category'));
      const requestedPage = this.parsePage(params.get('page'));

      this.searchText.set(search);
      this.selectedCategory.set(search ? 'ALL' : category ?? 'ALL');
      this.currentPage.set(requestedPage);

      if (search) {
        this.loadProducts(this.catalog.search(search));
      } else if (category) {
        this.loadProducts(this.catalog.byCategory(category));
      } else {
        this.loadProducts(this.catalog.list());
      }
    });
  }

  selectCategory(category: ProductCategory | 'ALL'): void {
    void this.navigate({ category: category === 'ALL' ? null : category, search: null, page: null });
  }

  submitSearch(text: string): void {
    const search = text.trim();
    void this.navigate({ search: search || null, category: null, page: null });
  }

  clearSearch(): void {
    void this.navigate({ search: null, category: null, page: null });
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages() || page === this.currentPage()) return;
    void this.navigate({ page: page === 1 ? null : page });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  retry(): void {
    if (this.searchText()) {
      this.loadProducts(this.catalog.search(this.searchText()));
    } else {
      const category = this.selectedCategory();
      this.loadProducts(category === 'ALL' ? this.catalog.list() : this.catalog.byCategory(category));
    }
  }

  useFallback(event: Event): void {
    const image = event.target as HTMLImageElement;
    if (image.dataset['fallbackApplied']) return;
    image.dataset['fallbackApplied'] = 'true';
    image.src = this.fallbackImage;
  }

  categoryLabel(category: ProductCategory): string {
    return {
      GADGETS: 'Gadgets de escritorio',
      GAMING: 'Retro gaming',
      SETUP_ACCESSORIES: 'Setup y accesorios',
      OTHERS: 'Otros hallazgos'
    }[category];
  }

  private loadProducts(request: Observable<Product[]>): void {
    this.loading.set(true);
    this.error.set(false);
    request.subscribe({
      next: (products) => {
        this.products.set(products);
        this.currentPage.set(Math.min(this.currentPage(), Math.max(1, Math.ceil(products.length / this.pageSize))));
        this.loading.set(false);
      },
      error: () => {
        this.products.set([]);
        this.error.set(true);
        this.loading.set(false);
      }
    });
  }

  private navigate(queryParams: Record<string, string | number | null>): Promise<boolean> {
    return this.router.navigate(['/catalogo'], {
      queryParams,
      queryParamsHandling: 'merge'
    });
  }

  private parseCategory(value: string | null): ProductCategory | null {
    const categories: ProductCategory[] = ['GADGETS', 'GAMING', 'SETUP_ACCESSORIES', 'OTHERS'];
    return categories.find((category) => category === value) ?? null;
  }

  private parsePage(value: string | null): number {
    const page = Number(value);
    return Number.isInteger(page) && page > 0 ? page : 1;
  }
}
