import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormArray, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Observable, of, switchMap } from 'rxjs';
import { AdminProductService } from './admin-product.service';
import { CreateProductRequest, Product, ProductCategory, UpdateProductRequest } from './product.model';

interface ApiValidationError {
  fieldErrors?: Array<{ field: string; message: string }>;
}

@Component({
  selector: 'rb-create-product',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './create-product.component.html',
  styleUrl: './create-product.component.css'
})
export class CreateProductComponent {
  private readonly adminProducts = inject(AdminProductService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly urlPattern = /^https?:\/\/.+/i;
  private readonly productId = this.route.snapshot.paramMap.get('id');
  private originalActive = true;

  readonly editing = signal(Boolean(this.productId));
  readonly loadingProduct = signal(Boolean(this.productId));
  readonly submitting = signal(false);
  readonly submitError = signal('');
  readonly fieldErrors = signal<Record<string, string>>({});
  readonly fallbackImage = 'assets/product-placeholder-retro-bazar-v6.png';
  readonly categories: Array<{ value: ProductCategory; label: string }> = [
    { value: 'GAMING', label: 'Retro gaming' },
    { value: 'GADGETS', label: 'Gadgets de escritorio' },
    { value: 'SETUP_ACCESSORIES', label: 'Setup y accesorios' },
    { value: 'OTHERS', label: 'Otros hallazgos' }
  ];

  readonly form = new FormGroup({
    name: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.maxLength(255)] }),
    brand: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.maxLength(255)] }),
    description: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.maxLength(2000)] }),
    price: new FormControl<number | null>(null, [Validators.required, Validators.min(0.01)]),
    stock: new FormControl<number | null>(0, [Validators.required, Validators.min(0)]),
    category: new FormControl<ProductCategory>('GAMING', { nonNullable: true, validators: [Validators.required] }),
    imageUrls: new FormArray<FormControl<string>>([
      new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.pattern(this.urlPattern)] })
    ]),
    active: new FormControl(true, { nonNullable: true })
  });

  constructor() {
    if (this.productId) this.loadProduct(this.productId);
  }

  get imageUrls(): FormArray<FormControl<string>> {
    return this.form.controls.imageUrls;
  }

  addImageUrl(): void {
    if (this.imageUrls.length >= 5) return;
    this.imageUrls.push(new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.pattern(this.urlPattern)]
    }));
  }

  removeImageUrl(index: number): void {
    if (this.imageUrls.length === 1) return;
    this.imageUrls.removeAt(index);
  }

  submit(): void {
    this.submitError.set('');
    this.fieldErrors.set({});
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const value = this.form.getRawValue();
    const request: CreateProductRequest = {
      name: value.name.trim(),
      brand: value.brand.trim(),
      description: value.description.trim(),
      price: value.price!,
      stock: value.stock!,
      category: value.category,
      imageUrls: value.imageUrls.map((url) => url.trim()),
      active: value.active
    };

    this.submitting.set(true);
    const saveRequest = this.productId
      ? this.updateProduct(this.productId, request)
      : this.adminProducts.create(request);

    saveRequest.subscribe({
      next: () => void this.router.navigate(['/admin/productos']),
      error: (response: { error?: ApiValidationError }) => {
        this.submitting.set(false);
        const errors = response.error?.fieldErrors ?? [];
        this.fieldErrors.set(Object.fromEntries(errors.map((error) => [error.field, error.message])));
        this.submitError.set(errors.length
          ? 'Revisa los campos indicados antes de volver a guardar.'
          : `No se ha podido ${this.editing() ? 'guardar' : 'crear'} el producto. Comprueba que el backend está activo.`);
      }
    });
  }

  useFallback(event: Event): void {
    const image = event.target as HTMLImageElement;
    image.src = this.fallbackImage;
  }

  private loadProduct(id: string): void {
    this.adminProducts.byId(id).subscribe({
      next: (product) => {
        this.originalActive = product.active;
        this.form.patchValue({
          name: product.name,
          brand: product.brand,
          description: product.description,
          price: product.price,
          stock: product.stock,
          category: product.category,
          active: product.active
        });
        this.imageUrls.clear();
        product.imageUrls.forEach((url) => this.imageUrls.push(this.createImageControl(url)));
        if (!product.imageUrls.length) this.imageUrls.push(this.createImageControl(''));
        this.loadingProduct.set(false);
      },
      error: () => {
        this.loadingProduct.set(false);
        this.submitError.set('No se ha podido cargar el producto que quieres editar.');
      }
    });
  }

  private updateProduct(id: string, request: CreateProductRequest): Observable<Product> {
    const updateRequest: UpdateProductRequest = {
      name: request.name,
      brand: request.brand,
      description: request.description,
      price: request.price,
      stock: request.stock,
      category: request.category,
      imageUrls: request.imageUrls
    };

    return this.adminProducts.update(id, updateRequest).pipe(
      switchMap((product) => {
        if (request.active === this.originalActive) return of(product);
        return request.active ? this.adminProducts.activate(id) : this.adminProducts.deactivate(id);
      })
    );
  }

  private createImageControl(value: string): FormControl<string> {
    return new FormControl(value, {
      nonNullable: true,
      validators: [Validators.required, Validators.pattern(this.urlPattern)]
    });
  }
}
