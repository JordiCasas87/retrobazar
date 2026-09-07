import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { CreateProductRequest, ProductCategory } from '../../../catalog/models/product.model';

export function createImageUrlControl(urlPattern: RegExp, value = ''): FormControl<string> {
  return new FormControl(value, {
    nonNullable: true,
    validators: [Validators.required, Validators.pattern(urlPattern)]
  });
}

export function createProductForm(urlPattern: RegExp) {
  return new FormGroup({
    name: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.maxLength(255)] }),
    brand: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.maxLength(255)] }),
    description: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.maxLength(2000)] }),
    price: new FormControl<number | null>(null, [Validators.required, Validators.min(0.01)]),
    stock: new FormControl<number | null>(0, [Validators.required, Validators.min(0)]),
    category: new FormControl<ProductCategory>('GAMING', { nonNullable: true, validators: [Validators.required] }),
    imageUrls: new FormArray<FormControl<string>>([createImageUrlControl(urlPattern)]),
    active: new FormControl(true, { nonNullable: true })
  });
}

export type ProductForm = ReturnType<typeof createProductForm>;

export function productRequestFromForm(form: ProductForm): CreateProductRequest {
  const value = form.getRawValue();
  return {
    name: value.name.trim(),
    brand: value.brand.trim(),
    description: value.description.trim(),
    price: value.price!,
    stock: value.stock!,
    category: value.category,
    imageUrls: value.imageUrls.map((url) => url.trim()),
    active: value.active
  };
}
