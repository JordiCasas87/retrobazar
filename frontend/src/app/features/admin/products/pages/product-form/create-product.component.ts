import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormArray, FormControl, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { finalize, forkJoin, Observable, of, Subscription, switchMap } from 'rxjs';
import { AdminProductService } from '../../data-access/admin-product.service';
import { createImageUrlControl, createProductForm, productRequestFromForm } from '../../forms/product-form';
import { PRODUCT_CATEGORY_OPTIONS } from '../../../../catalog/models/product.constants';
import {
  CreateProductRequest,
  Product,
  ProductAgentResponse,
  UploadedProductImage,
  UpdateProductRequest
} from '../../../../catalog/models/product.model';

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
  private agentAnalysisSubscription?: Subscription;

  readonly editing = signal(Boolean(this.productId));
  readonly loadingProduct = signal(Boolean(this.productId));
  readonly submitting = signal(false);
  readonly uploadingImages = signal(false);
  readonly imageUploadError = signal('');
  readonly cloudinaryLibraryOpen = signal(false);
  readonly cloudinaryLibraryLoading = signal(false);
  readonly cloudinaryLibraryError = signal('');
  readonly cloudinaryImages = signal<UploadedProductImage[]>([]);
  readonly submitError = signal('');
  readonly fieldErrors = signal<Record<string, string>>({});
  readonly agentModalOpen = signal(false);
  readonly agentLoading = signal(false);
  readonly agentError = signal('');
  readonly agentProposal = signal<ProductAgentResponse | null>(null);
  readonly agentTitleDraft = signal('');
  readonly agentDescriptionDraft = signal('');
  readonly agentPriceDraft = signal<number | null>(null);
  readonly fallbackImage = 'assets/product-placeholder-retro-bazar-v6.jpg';
  readonly categories = PRODUCT_CATEGORY_OPTIONS;

  readonly form = createProductForm(this.urlPattern);

  constructor() {
    if (this.productId) this.loadProduct(this.productId);
  }

  get imageUrls(): FormArray<FormControl<string>> {
    return this.form.controls.imageUrls;
  }

  get imageStorageKeys(): FormArray<FormControl<string | null>> {
    return this.form.controls.imageStorageKeys;
  }

  get primaryImageUrl(): string {
    return this.imageUrls.controls[0]?.value || this.fallbackImage;
  }

  removeImageUrl(index: number): void {
    this.imageUrls.removeAt(index);
    this.imageStorageKeys.removeAt(index);
  }

  moveImage(index: number, direction: -1 | 1): void {
    const target = index + direction;
    if (target < 0 || target >= this.imageUrls.length) return;

    const url = this.imageUrls.at(index);
    const storageKey = this.imageStorageKeys.at(index);
    this.imageUrls.removeAt(index);
    this.imageStorageKeys.removeAt(index);
    this.imageUrls.insert(target, url);
    this.imageStorageKeys.insert(target, storageKey);
  }

  openCloudinaryLibrary(): void {
    this.cloudinaryLibraryOpen.set(true);
    if (this.cloudinaryImages().length) return;

    this.cloudinaryLibraryLoading.set(true);
    this.cloudinaryLibraryError.set('');
    this.adminProducts.listCloudinaryImages()
      .pipe(finalize(() => this.cloudinaryLibraryLoading.set(false)))
      .subscribe({
        next: (images) => this.cloudinaryImages.set(images),
        error: () => this.cloudinaryLibraryError.set(
          'No se ha podido leer la carpeta Retro Bazar Products de Cloudinary.'
        )
      });
  }

  closeCloudinaryLibrary(): void {
    this.cloudinaryLibraryOpen.set(false);
  }

  toggleCloudinaryImage(image: UploadedProductImage): void {
    const selectedIndex = this.imageStorageKeys.controls.findIndex(
      (control) => control.value === image.storageKey
    );
    if (selectedIndex >= 0) {
      this.removeImageUrl(selectedIndex);
      return;
    }

    if (this.imageUrls.length >= 5) return;
    this.imageUrls.push(createImageUrlControl(this.urlPattern, image.url));
    this.imageStorageKeys.push(new FormControl(image.storageKey));
  }

  hasSelectedImage(image: UploadedProductImage): boolean {
    return this.imageStorageKeys.controls.some((control) => control.value === image.storageKey);
  }

  isPrimaryImage(image: UploadedProductImage): boolean {
    return this.imageStorageKeys.controls[0]?.value === image.storageKey;
  }

  selectImages(event: Event): void {
    const input = event.target as HTMLInputElement;
    const selected = Array.from(input.files ?? []);
    input.value = '';
    this.imageUploadError.set('');

    const availableSlots = 5 - this.imageUrls.length;
    if (!selected.length || availableSlots <= 0) return;
    if (selected.length > availableSlots) {
      this.imageUploadError.set(`Solo puedes añadir ${availableSlots} imagen${availableSlots === 1 ? '' : 'es'} más.`);
      return;
    }

    this.uploadingImages.set(true);
    forkJoin(selected.map((file) => this.adminProducts.uploadImage(file)))
      .pipe(finalize(() => this.uploadingImages.set(false)))
      .subscribe({
        next: (images) => images.forEach((image) => {
          this.imageUrls.push(createImageUrlControl(this.urlPattern, image.url));
          this.imageStorageKeys.push(new FormControl(image.storageKey));
        }),
        error: () => this.imageUploadError.set(
          'No se han podido subir las imágenes. Comprueba Cloudinary y vuelve a intentarlo.'
        )
      });
  }

  openAgentModal(): void {
    this.agentError.set('');
    this.agentProposal.set(null);

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.agentError.set('Completa todos los campos obligatorios antes de pedir una propuesta.');
      this.agentModalOpen.set(true);
      return;
    }

    this.agentModalOpen.set(true);
    this.agentLoading.set(true);

    const value = this.form.getRawValue();
    this.agentAnalysisSubscription?.unsubscribe();
    this.agentAnalysisSubscription = this.adminProducts.analyzeWithAgent({
      title: value.name.trim(),
      brand: value.brand.trim(),
      description: value.description.trim(),
      currentPrice: value.price!,
      category: value.category,
      imageUrls: value.imageUrls.map((url) => url.trim())
    }).subscribe({
      next: (proposal) => {
        this.agentProposal.set(proposal);
        this.agentTitleDraft.set(proposal.suggestedTitle);
        this.agentDescriptionDraft.set(proposal.suggestedDescription);
        this.agentPriceDraft.set(proposal.suggestedPrice);
        this.agentLoading.set(false);
      },
      error: () => {
        this.agentLoading.set(false);
        this.agentError.set('No hemos podido analizar la imagen. Puedes continuar manualmente o intentarlo de nuevo.');
      }
    });
  }

  cancelAgentAnalysis(): void {
    this.agentAnalysisSubscription?.unsubscribe();
    this.agentAnalysisSubscription = undefined;
    this.agentLoading.set(false);
    this.agentProposal.set(null);
    this.agentError.set('');
    this.agentModalOpen.set(false);
  }

  closeAgentModal(): void {
    if (this.agentLoading()) return;
    this.agentModalOpen.set(false);
  }

  applyAgentProposal(): void {
    const proposal = this.agentProposal();
    if (!proposal) return;

    this.form.patchValue({
      name: this.agentTitleDraft(),
      description: this.agentDescriptionDraft(),
      price: this.agentPriceDraft()
    });
    this.agentModalOpen.set(false);
  }

  submit(): void {
    this.submitError.set('');
    this.fieldErrors.set({});
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const request = productRequestFromForm(this.form);

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
          wallapopUrl: product.wallapopUrl ?? '',
          active: product.active
        });
        this.imageUrls.clear();
        product.imageUrls.forEach((url) => this.imageUrls.push(createImageUrlControl(this.urlPattern, url)));
        this.imageStorageKeys.clear();
        product.imageUrls.forEach((_, index) => this.imageStorageKeys.push(
          new FormControl(product.imageStorageKeys?.[index] ?? null)
        ));
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
      imageUrls: request.imageUrls,
      imageStorageKeys: request.imageStorageKeys,
      wallapopUrl: request.wallapopUrl
    };

    return this.adminProducts.update(id, updateRequest).pipe(
      switchMap((product) => {
        if (request.active === this.originalActive) return of(product);
        return request.active ? this.adminProducts.activate(id) : this.adminProducts.deactivate(id);
      })
    );
  }
}
