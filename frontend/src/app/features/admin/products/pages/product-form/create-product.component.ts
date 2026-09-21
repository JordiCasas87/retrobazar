import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormArray, FormControl, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Observable, of, Subscription, switchMap } from 'rxjs';
import { AdminProductService } from '../../data-access/admin-product.service';
import { createImageUrlControl, createProductForm, productRequestFromForm } from '../../forms/product-form';
import { PRODUCT_CATEGORY_OPTIONS } from '../../../../catalog/models/product.constants';
import {
  CreateProductRequest,
  Product,
  ProductAgentResponse,
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
  readonly submitError = signal('');
  readonly fieldErrors = signal<Record<string, string>>({});
  readonly agentModalOpen = signal(false);
  readonly agentLoading = signal(false);
  readonly agentError = signal('');
  readonly agentProposal = signal<ProductAgentResponse | null>(null);
  readonly agentTitleDraft = signal('');
  readonly agentDescriptionDraft = signal('');
  readonly agentPriceDraft = signal<number | null>(null);
  readonly fallbackImage = 'assets/product-placeholder-retro-bazar-v6.png';
  readonly categories = PRODUCT_CATEGORY_OPTIONS;

  readonly form = createProductForm(this.urlPattern);

  constructor() {
    if (this.productId) this.loadProduct(this.productId);
  }

  get imageUrls(): FormArray<FormControl<string>> {
    return this.form.controls.imageUrls;
  }

  addImageUrl(): void {
    if (this.imageUrls.length >= 5) return;
    this.imageUrls.push(createImageUrlControl(this.urlPattern));
  }

  removeImageUrl(index: number): void {
    if (this.imageUrls.length === 1) return;
    this.imageUrls.removeAt(index);
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
          active: product.active
        });
        this.imageUrls.clear();
        product.imageUrls.forEach((url) => this.imageUrls.push(createImageUrlControl(this.urlPattern, url)));
        if (!product.imageUrls.length) this.imageUrls.push(createImageUrlControl(this.urlPattern));
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
}
