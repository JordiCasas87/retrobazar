import { ProductCategory } from './product.model';

export const PRODUCT_CATEGORY_OPTIONS: ReadonlyArray<{ value: ProductCategory; label: string }> = [
  { value: 'GAMING', label: 'Retro gaming' },
  { value: 'GADGETS', label: 'Gadgets de escritorio' },
  { value: 'SETUP_ACCESSORIES', label: 'Setup y accesorios' },
  { value: 'OTHERS', label: 'Otros hallazgos' }
];

export function isProductCategory(value: string | null): value is ProductCategory {
  return PRODUCT_CATEGORY_OPTIONS.some((category) => category.value === value);
}

export function productCategoryLabel(category: ProductCategory): string {
  return PRODUCT_CATEGORY_OPTIONS.find((option) => option.value === category)?.label ?? category;
}
