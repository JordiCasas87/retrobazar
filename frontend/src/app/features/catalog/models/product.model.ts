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

export interface ProductAgentRequest {
  title: string;
  brand: string;
  description: string;
  currentPrice: number;
  category: ProductCategory;
  imageUrls: string[];
}

export interface SameBrandProduct {
  productId: string;
  title: string;
  price: number;
  imageUrl: string;
}

export interface InternetProductReference {
  title: string;
  url: string;
  price: number;
}

export interface ProductAgentResponse {
  suggestedTitle: string;
  suggestedDescription: string;
  suggestedPrice: number;
  internetReferences: InternetProductReference[];
  sameBrandProducts: SameBrandProduct[];
}
