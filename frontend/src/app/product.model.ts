export type ProductCategory = 'GADGETS' | 'GAMING' | 'SETUP_ACCESSORIES' | 'OTHERS';

export interface Product {
  id: string;
  name: string;
  brand: string;
  description: string;
  price: number;
  stock: number;
  category: ProductCategory;
  imageUrls: string[];
  active: boolean;
  createdAt: string;
}

export interface CreateProductRequest {
  name: string;
  brand: string;
  description: string;
  price: number;
  stock: number;
  category: ProductCategory;
  imageUrls: string[];
  active: boolean;
}

export type UpdateProductRequest = Omit<CreateProductRequest, 'active'>;
